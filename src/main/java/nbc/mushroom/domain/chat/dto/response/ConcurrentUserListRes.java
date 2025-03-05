package nbc.mushroom.domain.chat.dto.response;

import java.util.List;
import nbc.mushroom.domain.user.entity.User;

public record ConcurrentUserListRes(
    Long chatRoomId,
    Long concurrentUserCount,
    List<UserInfoRes> userInfoRes
) {

    public static ConcurrentUserListRes from(Long chatRoomId, List<UserInfoRes> userInfoResList) {
        return new ConcurrentUserListRes(
            chatRoomId,
            (long) userInfoResList.size(),
            userInfoResList
        );
    }

    public record UserInfoRes(
        Long userId,
        String nickname,
        String imageUrl
    ) {

        public static UserInfoRes from(User user) {
            return new UserInfoRes(
                user.getId(),
                user.getNickname(),
                user.getImageUrl()
            );
        }
    }
}
