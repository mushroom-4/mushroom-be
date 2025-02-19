package nbc.mushroom.domain.user.dto.response;

import nbc.mushroom.domain.user.entity.User;

public record UserRes(
    Long id,
    String email,
    String nickname,
    String imageUrl
) {

    public static UserRes from(User user) {
        return new UserRes(user.getId(), user.getEmail(), user.getNickname(), user.getImageUrl());
    }
}
