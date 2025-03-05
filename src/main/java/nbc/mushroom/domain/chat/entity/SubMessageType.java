package nbc.mushroom.domain.chat.entity;

import static nbc.mushroom.domain.common.exception.ExceptionType.SUB_MESSAGE_TYPE_NOT_FOUND;

import com.fasterxml.jackson.annotation.JsonCreator;
import nbc.mushroom.domain.common.exception.CustomException;

public enum SubMessageType {
    CHAT_MESSAGE,
    CONCURRENT_USER_LIST;

    @JsonCreator // JSON → 객체 (역직렬화)
    public static SubMessageType fromValue(String value) {
        try {
            return SubMessageType.valueOf(value);
        } catch (RuntimeException e) {
            throw new CustomException(SUB_MESSAGE_TYPE_NOT_FOUND);
        }
    }
}
