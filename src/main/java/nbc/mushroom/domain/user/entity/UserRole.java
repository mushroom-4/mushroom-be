package nbc.mushroom.domain.user.entity;

import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_USER_ROLE;

import java.util.Arrays;
import nbc.mushroom.domain.common.exception.CustomException;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
            .filter(r -> r.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new CustomException(INVALID_USER_ROLE));
    }
}
