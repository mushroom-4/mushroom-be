package nbc.mushroom.domain.like.service;


import static nbc.mushroom.domain.common.exception.ExceptionType.EXIST_LIKE_BY_AUCTION_ITEM;
import static nbc.mushroom.domain.common.exception.ExceptionType.LIKE_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.NOT_SELF_LIKE;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.like.entity.Like;
import nbc.mushroom.domain.like.repository.LikeRepository;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final LikeRepository likeRepository;

    public void createLike(Long userId, Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (auctionItemRepository.existsByUserAndAuctionItem(user, auctionItemId)) {
            throw new CustomException(NOT_SELF_LIKE);
        }

        if (likeRepository.findLikeByUserAndAuctionItem(user,
            auctionItem).isPresent()) {
            throw new CustomException(EXIST_LIKE_BY_AUCTION_ITEM);
        }

        Like like = Like.builder()
            .auctionItem(auctionItem)
            .user(user)
            .build();

        likeRepository.save(like);
    }

    public void hardDeleteLike(Long userId, Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Like like = likeRepository.findLikeByUserAndAuctionItem(user,
            auctionItem).orElseThrow(() -> new CustomException(LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }

}
