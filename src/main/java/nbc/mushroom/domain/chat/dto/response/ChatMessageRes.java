package nbc.mushroom.domain.chat.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.chat.entity.ChatMessage;
import nbc.mushroom.domain.chat.entity.MessageType;

public record ChatMessageRes(
    Long chatRoomId,
    MessageType messageType,
    Long senderId,
    String imageUrl,
    String nickname,
    String message,
    LocalDateTime sendDateTime

) {

    public static ChatMessageRes from(ChatMessage chatMessage) {
        return new ChatMessageRes(
            chatMessage.getChatRoomId(),
            chatMessage.getMessageType(),
            chatMessage.getSender().getId(),
            chatMessage.getSender().getImageUrl(),
            chatMessage.getSender().getNickname(),
            chatMessage.getMessage(),
            chatMessage.getSendDateTime()
        );
    }
}
