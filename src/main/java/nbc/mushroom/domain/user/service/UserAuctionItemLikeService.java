package nbc.mushroom.domain.user.service;


import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.like.repository.AuctionItemLikeRepository;
import nbc.mushroom.domain.user.dto.response.SearchUserAuctionItemLikeRes;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuctionItemLikeService {

    private final AuctionItemLikeRepository auctionItemLikeRepository;

    // 본인이 누른 경매 물품 좋아요
    public Page<SearchUserAuctionItemLikeRes> searchUserLikedAuctionItems(User user,
        Pageable pageable) {

        return auctionItemLikeRepository.findAuctionItemLikeByUserId(
            user, pageable);
    }

}
