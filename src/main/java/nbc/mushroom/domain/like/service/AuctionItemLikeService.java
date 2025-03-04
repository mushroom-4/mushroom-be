package nbc.mushroom.domain.like.service;


import static nbc.mushroom.domain.common.exception.ExceptionType.EXIST_LIKE_BY_AUCTION_ITEM;
import static nbc.mushroom.domain.common.exception.ExceptionType.LIKE_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.NOT_SELF_LIKE;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.like.dto.response.CheckLikedAuctionItemRes;
import nbc.mushroom.domain.like.entity.AuctionItemLike;
import nbc.mushroom.domain.like.repository.AuctionItemLikeRepository;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionItemLikeService {

    private final UserRepository userRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final AuctionItemLikeRepository auctionItemLikeRepository;

    public void createAuctionItemLike(User user, Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        if (auctionItemRepository.existsByUserAndAuctionItem(user, auctionItemId)) {
            throw new CustomException(NOT_SELF_LIKE);
        }

        if (auctionItemLikeRepository.findLikeByUserAndAuctionItem(user,
            auctionItem).isPresent()) {
            throw new CustomException(EXIST_LIKE_BY_AUCTION_ITEM);
        }

        AuctionItemLike auctionItemLike = AuctionItemLike.builder()
            .auctionItem(auctionItem)
            .user(user)
            .build();

        auctionItemLikeRepository.save(auctionItemLike);
    }

    // 본인이 해당 경매 물품에, 좋아요를 한 여부를 확인
    public CheckLikedAuctionItemRes getLikedAuctionItem(User user, Long auctionItemId) {

        Boolean hasLike = auctionItemLikeRepository.existAuctionItemLikeByUserAndAuctionItem(
            user, auctionItemId);

        return new CheckLikedAuctionItemRes(hasLike);
    }

    public void deleteAuctionItemLike(User user, Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        AuctionItemLike auctionItemLike = auctionItemLikeRepository.findLikeByUserAndAuctionItem(
            user,
            auctionItem).orElseThrow(() -> new CustomException(LIKE_NOT_FOUND));

        auctionItemLikeRepository.delete(auctionItemLike);
    }

}
