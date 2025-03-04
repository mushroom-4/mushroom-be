package nbc.mushroom.domain.chat.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.user.entity.User;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    private Long chatRoomId;

    private MessageType messageType;

    private User sender;

    private String message;

    private LocalDateTime sendDateTime;
}
