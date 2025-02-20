package nbc.mushroom.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.dto.request.ChatMessageReq;
import nbc.mushroom.domain.chat.dto.response.ChatMessageRes;
import nbc.mushroom.domain.chat.service.ChatService;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatControllerV2 {

    private final ChatService chatService;

    @MessageMapping("/chats/{chatRoomId}")
    public ChatMessageRes sendChatMessage(
        @DestinationVariable Long chatRoomId,
        @Payload ChatMessageReq chatMessageReq,
        @Auth AuthUser authUser
    ) {
        log.info("Auth User ID : {} ", authUser.id());
        return chatService.sendChatMessage(chatRoomId, chatMessageReq, User.fromAuthUser(authUser));
    }
}
