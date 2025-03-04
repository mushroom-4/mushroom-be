package nbc.mushroom.config.websocket;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static nbc.mushroom.domain.common.exception.ExceptionType.BIDDING_REQUIRED;
import static nbc.mushroom.domain.common.exception.ExceptionType.CHAT_ROOM_NOT_FOUND;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.service.AuctionItemService;
import nbc.mushroom.domain.bid.service.BidService;
import nbc.mushroom.domain.chat.service.ChatRoomService;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.JwtUtil;
import nbc.mushroom.domain.common.util.StompUtil;
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
    private final ChatRoomService chatRoomService;
    private final JwtUtil jwtUtil;

    /**
     * STOMP 메시지가 전송되기 전에 호출
     * <p>
     * STOMP 명령어에 따라 필요한 작업 처리 - SUBSCRIBE: 클라이언트가 특정 토픽을 구독할 때 처리. - SEND: 클라이언트가 메시지를 보낼 때 처리.
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
            case CONNECT:
                handleConnect(stompHeaderAccessor);
                break;
            case SUBSCRIBE:
                handleSubscribe(stompHeaderAccessor);
                break;
            case SEND:
                handleSend(stompHeaderAccessor);
                break;
            case DISCONNECT:
                handleDisconnect(stompHeaderAccessor);
                break;
            default:
                break;
        }
        return message;
    }

    /**
     * STOMP CONNECT 요청 시, JWT 토큰 검증 및 사용자 정보 저장
     */
    private void handleConnect(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: CONNECT 요청 감지 ::::");

        String bearerJwt = getAuthorizationHeader(stompHeaderAccessor);

        log.info("✅ 받은 토큰: {}", bearerJwt);

        Map<String, Object> userInfo = jwtUtil.getUserInfoFromTokenForWebSocket(bearerJwt);

        stompHeaderAccessor.getSessionAttributes().putAll(userInfo);

        log.info("✅ CONNECT 성공, STOMP 세션 저장 확인: {}",
            stompHeaderAccessor.getSessionAttributes());
    }

    /**
     * 채팅방 구독 요청 시
     * 해당 경매 물품이 존재하는지 확인 ( chatRoomId == auctionItemId )
     * 접속자 목록에 유저Id, 세션Id 추가
     */
    private void handleSubscribe(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: SUBSCRIBE 요청 감지 ::::");
        try {
            Long userId = StompUtil.getUserId(stompHeaderAccessor);

            log.info("✅ SUBSCRIBE 요청 destination: {}", stompHeaderAccessor.getDestination());

            Long chatRoomId = StompUtil.getChatRoomId(stompHeaderAccessor, "/ws/sub");
            if (FALSE.equals(auctionItemService.hasAuctionItem(chatRoomId))) {
                throw new CustomException(CHAT_ROOM_NOT_FOUND);
            }

            stompHeaderAccessor.getSessionAttributes().put("chatRoomId", chatRoomId.toString());

            chatRoomService.addSessionId(chatRoomId.toString(), userId.toString(),
                stompHeaderAccessor.getSessionId());

            log.info("✅ SUBSCRIBE 성공: userId={}, chatRoomId={}", userId, chatRoomId);
        } catch (Exception e) {
            log.error("❌ SUBSCRIBE 실패: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 메시지 전송 시 해당 경매에 입찰한 기록이 있는 유저인지 확인
     *
     * 없으면 Error 메시지를 세션에 저장
     * 웹소켓 연결 중 권한이 생기면 세션에 저장된 Error 메시지 삭제
     */
    private void handleSend(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: SEND 요청 감지 ::::");
        try {
            Long chatRoomId = StompUtil.getChatRoomId(stompHeaderAccessor, "/ws/pub");
            Long loginUserId = StompUtil.getUserId(stompHeaderAccessor);

            boolean hasBid = TRUE.equals(bidService.hasBid(loginUserId, chatRoomId));

            validateBidAndSessionAttributeError(stompHeaderAccessor, hasBid);

            log.info("✅ SEND 성공: userId={}, chatRoomId={}", loginUserId, chatRoomId);
        } catch (Exception e) {
            log.error("❌ SEND 실패: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Disconnect 시 채팅방 접속자 목록에서 제거
     */
    private void handleDisconnect(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: DISCONNECT 요청 감지 ::::");

        Long loginUserId = StompUtil.getUserId(stompHeaderAccessor);

        String chatRoomIdStr = (String) stompHeaderAccessor.getSessionAttributes()
            .get("chatRoomId");

        String sessionId = stompHeaderAccessor.getSessionId();

        chatRoomService.removeSessionId(chatRoomIdStr, loginUserId.toString(),
            sessionId);

        log.info("DISCONNECT 성공: userId={}, chatRoomId={}, sessionId={}", loginUserId,
            chatRoomIdStr, sessionId);
    }

    /**
     * STOMP 헤더에서 `Authorization` 값을 가져옴
     */
    private String getAuthorizationHeader(StompHeaderAccessor accessor) {
        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        return (authHeaders != null && !authHeaders.isEmpty()) ? authHeaders.get(0) : null;
    }

    /**
     * 세션에서 입찰 여부에 따라 에러 메시지를 추가하거나 삭제하는 메서드
     *
     * 입찰하지 않은 경우: 에러 메시지를 세션에 추가
     * 입찷한 경우 & 세션에 에러메시지가 존재하는 경우 : 기존에 존재하는 에러 메시지를 삭제
     */
    private void validateBidAndSessionAttributeError(StompHeaderAccessor stompHeaderAccessor,
        boolean hasBid) {
        Map<String, Object> sessionAttributes = stompHeaderAccessor.getSessionAttributes();

        if (!hasBid) {
            log.error("입찰 필요: {}", BIDDING_REQUIRED.getMessage());
            sessionAttributes.put("error", BIDDING_REQUIRED.getMessage());
            return; // 이후 로직 실행할 필요 없음
        }

        if (sessionAttributes.get("error") != null) {
            log.info("입찰 내역이 확인됨 - 기존 에러 메시지 삭제");
            sessionAttributes.remove("error");
        }
    }
}
