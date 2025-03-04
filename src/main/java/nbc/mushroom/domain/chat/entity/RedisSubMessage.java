package nbc.mushroom.domain.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisSubMessage {

    private SubMessageType subMessageType;

    private Object data;

}
