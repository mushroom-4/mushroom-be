package nbc.mushroom.domain.like.service;


import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.like.dto.response.CheckLikedAuctionItemRes;
import nbc.mushroom.domain.like.dto.response.LikedAuctionItemRes;
import nbc.mushroom.domain.like.repository.AuctionItemLikeRepository;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedAuctionItemService {

    private final AuctionItemLikeRepository auctionItemLikeRepository;

    // 본인이 누른 경매 물품 좋아요
    public Page<LikedAuctionItemRes> getAllLikedAuctionItem(User user,
        Pageable pageable) {

        return auctionItemLikeRepository.findAuctionItemLikeByUserId(
            user, pageable);
    }

    // 본인이 해당 경매 물품에, 좋아요를 한 여부를 확인
    public CheckLikedAuctionItemRes getLikedAuctionItem(User user, Long auctionItemId) {

        Boolean hasLike = auctionItemLikeRepository.existAuctionItemLikeByUserAndAuctionItem(
            user, auctionItemId);

        return new CheckLikedAuctionItemRes(!hasLike);
    }

}
