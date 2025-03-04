package nbc.mushroom.domain.like.service;


import lombok.RequiredArgsConstructor;
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

    // 본인이 누른 경매 물품 좋아요 목록
    public Page<LikedAuctionItemRes> getAllLikedAuctionItem(User user,
        Pageable pageable) {

        return auctionItemLikeRepository.findAuctionItemLikeByUserId(
            user, pageable);
    }

}
