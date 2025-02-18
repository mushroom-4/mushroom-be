package nbc.mushroom.domain.user.service;


import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.like.repository.LikeRepository;
import nbc.mushroom.domain.user.dto.response.SearchUserAuctionItemLikeRes;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuctionItemLikeService {

    private final LikeRepository likeRepository;

    // 본인이 누른 경매 물품 좋아요
    public Page<SearchUserAuctionItemLikeRes> searchUserLike(User user, Pageable pageable) {

        return likeRepository.findAuctionItemLikeByUserId(
            user, pageable);
    }

}
