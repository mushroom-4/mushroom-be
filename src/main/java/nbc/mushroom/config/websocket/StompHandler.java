package nbc.mushroom.config.websocket;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUTH_TOKEN_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.BIDDING_REQUIRED;
import static nbc.mushroom.domain.common.exception.ExceptionType.CHAT_ROOM_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_CHAT_ROOM_PATH;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.service.AuctionItemService;
import nbc.mushroom.domain.bid.service.BidService;
import nbc.mushroom.domain.common.exception.CustomException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

// 메시지 전송을 가로채서 권한 검증
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final BidService bidService;
    private final AuctionItemService auctionItemService;

    /**
     * STOMP 메시지가 전송되기 전에 호출
     *
     * STOMP 명령어에 따라 필요한 작업 처리
     * - SUBSCRIBE: 클라이언트가 특정 토픽을 구독할 때 처리.
     * - SEND: 클라이언트가 메시지를 보낼 때 처리.
     *
     * @param message : 클라이언트가 전송한 메시지
     * @param channel : 메시지가 전달될 채널
     * @return message : 메시지 수정 작업 없어서 들어온거 그대로 반환함
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = stompHeaderAccessor.getCommand();

        if (command == null) {
            return message;
        }

        switch (command) {
            case SUBSCRIBE:
                handleSubscribe(stompHeaderAccessor);
                break;
            case SEND:
                handleSend(stompHeaderAccessor);
                break;
            default:
                break;
        }
        return message;
    }

    /**
     * 채팅방 구독 요청 시
     * 해당 경매 물품이 존재하는지 확인 ( chatRoomId == auctionItemId )
     */
    private void handleSubscribe(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: SUBSCRIBE 요청 감지 ::::");
        try {
            Long userId = (Long) stompHeaderAccessor.getSessionAttributes().get("userId");

            if (userId == null) {
                throw new CustomException(AUTH_TOKEN_NOT_FOUND);
            }

            Long chatRoomId = getChatRoomId(stompHeaderAccessor, "/sub");
            if (Boolean.FALSE.equals(auctionItemService.hasAuctionItem(chatRoomId))) {
                throw new CustomException(CHAT_ROOM_NOT_FOUND);
            }

            log.info("✅ SUBSCRIBE 성공: userId={}, chatRoomId={}", userId, chatRoomId);
        } catch (Exception e) {
            log.error("❌ SUBSCRIBE 실패: {}", e.getMessage());
            throw e; // 예외를 던져야 STOMP에서 처리 가능
        }
    }

    /**
     * 메시지 전송 시
     * 해당 경매에 입찰한 기록이 있는 유저인지 확인
     */
    private void handleSend(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: SEND 요청 감지 ::::");
        try {
            Long chatRoomId = getChatRoomId(stompHeaderAccessor, "/pub");
            Long loginUserId = (Long) stompHeaderAccessor.getSessionAttributes().get("userId");

            if (loginUserId == null) {
                throw new CustomException(AUTH_TOKEN_NOT_FOUND);
            }

            if (Boolean.FALSE.equals(bidService.hasBid(loginUserId, chatRoomId))) {
                throw new CustomException(BIDDING_REQUIRED);
            }

            log.info("✅ SEND 성공: userId={}, chatRoomId={}", loginUserId, chatRoomId);
        } catch (Exception e) {
            log.error("❌ SEND 실패: {}", e.getMessage());
            throw e; // 예외를 던져야 STOMP에서 처리 가능
        }
    }

    /**
     * STOMP 메시지의 Destination에서 채팅방 ID 추출 후 검증
     */
    private Long getChatRoomId(StompHeaderAccessor stompHeaderAccessor, String prefix) {
        String destination = stompHeaderAccessor.getDestination();
        if (destination == null || !destination.startsWith(prefix + "/chats/")) {
            throw new CustomException(INVALID_CHAT_ROOM_PATH);
        }

        String chatRoomIdStr = destination.substring((prefix + "/chats/").length());
        if (chatRoomIdStr.isEmpty() || !chatRoomIdStr.matches("\\d+")) {
            throw new CustomException(INVALID_CHAT_ROOM_PATH);
        }

        return Long.parseLong(chatRoomIdStr);
    }
}