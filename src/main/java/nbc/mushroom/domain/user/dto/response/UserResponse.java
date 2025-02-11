package nbc.mushroom.domain.user.dto.response;

import nbc.mushroom.domain.user.entity.User;

public record UserResponse(
    Long id,
    String email
) {

    public static UserResponse toDto(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }
}
