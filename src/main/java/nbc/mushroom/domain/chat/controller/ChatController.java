package nbc.mushroom.domain.chat.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.dto.request.ChatMessageReq;
import nbc.mushroom.domain.chat.dto.response.ChatMessageRes;
import nbc.mushroom.domain.chat.service.ChatRoomService;
import nbc.mushroom.domain.chat.service.ChatService;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chats/{chatRoomId}")
    public ChatMessageRes sendChatMessage(
        @DestinationVariable Long chatRoomId,
        @Valid @Payload ChatMessageReq chatMessageReq,
        StompHeaderAccessor stompHeaderAccessor,
        @Auth AuthUser authUser
    ) {
        log.info("Auth User ID : {} ", authUser.id());
        return chatService.sendChatMessage(chatRoomId, chatMessageReq, stompHeaderAccessor,
            User.fromAuthUser(authUser));
    }

    @GetMapping("/api/bids/chats/{chatRoomId}")
    public ResponseEntity<ApiResponse<List<ChatMessageRes>>> getChatHistory(
        @PathVariable Long chatRoomId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("이전 채팅 내역이 성공적으로 조회되었습니다.",
                chatService.getChatHistory(chatRoomId)));
    }
}
