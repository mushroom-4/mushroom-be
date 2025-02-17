package nbc.mushroom.domain.user.service;


import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.like.repository.LikeRepository;
import nbc.mushroom.domain.user.dto.response.SearchUserAuctionItemLikeRes;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuctionItemLikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public Page<SearchUserAuctionItemLikeRes> searchUserLike(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
        return likeRepository.findAuctionItemLikeByUserId(
            user, pageable);
    }

}
