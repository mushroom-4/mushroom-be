package nbc.mushroom.domain.auction_item.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_NOT_USER;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.dto.request.CreateAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.request.PutAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.image.ImageUtil;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;
    private final UserRepository userRepository;
    private final ImageUtil imageUtil;

    public SearchAuctionItemRes searchAuctionItem(long auctionItemId) {
        AuctionItem searchAuctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);
        return SearchAuctionItemRes.from(searchAuctionItem);
    }

    public Page<SearchAuctionItemRes> findAllAuctionItems(Pageable pageable) {
        return auctionItemRepository.findAllAuctionItems(pageable);
    }

    @Transactional
    public AuctionItemRes createAuctionItem(Long userId,
        CreateAuctionItemReq createAuctionItemReq) {
        User user = validateUserById(userId);

        String fileName = imageUtil.upload(createAuctionItemReq.image());
        String imageUrl = imageUtil.getImageUrl(fileName);

        AuctionItem auctionItem = AuctionItem.builder()
            .seller(user)
            .name(createAuctionItemReq.name())
            .description(createAuctionItemReq.description())
            .brand(createAuctionItemReq.brand())
            .imageUrl(fileName)
            .size(createAuctionItemReq.auctionItemSize())
            .category(createAuctionItemReq.auctionItemCategory())
            .startPrice(createAuctionItemReq.startPrice())
            .startTime(createAuctionItemReq.startTime())
            .endTime(createAuctionItemReq.endTime())
            .build();

        auctionItemRepository.save(auctionItem);

        return AuctionItemRes.from(auctionItem, imageUrl);
    }

    @Transactional
    public AuctionItemRes updateAuctionItem(Long userId, Long auctionItemId,
        PutAuctionItemReq putAuctionItemReq) {

        AuctionItem auctionItem = validateItemById(userId, auctionItemId);

        auctionItemRepository.findAuctionItemById(auctionItemId);

        User user = validateUserById(userId);

        if (putAuctionItemReq.image() != null) {
            imageUtil.delete(auctionItem.getImageUrl());
        }

        String fileName = imageUtil.upload(putAuctionItemReq.image());
        String updateImageUrl = imageUtil.getImageUrl(fileName);

        AuctionItem updateAuctionItem = AuctionItem.builder()
            .id(auctionItemId)
            .seller(user)
            .name(putAuctionItemReq.name())
            .description(putAuctionItemReq.description())
            .brand(putAuctionItemReq.brand())
            .imageUrl(fileName)
            .size(putAuctionItemReq.auctionItemSize())
            .category(putAuctionItemReq.auctionItemCategory())
            .startPrice(putAuctionItemReq.startPrice())
            .startTime(putAuctionItemReq.startTime())
            .endTime(putAuctionItemReq.endTime())
            .build();

        auctionItemRepository.save(updateAuctionItem);

        return AuctionItemRes.from(updateAuctionItem, updateImageUrl);
    }

    @Transactional
    public void softDeleteAuctionItem(Long userId, Long auctionItemId) {

        AuctionItem auctionItem = validateItemById(userId, auctionItemId);

        validateUserById(userId);

        auctionItem.softDelete();
    }

    private AuctionItem validateItemById(Long userId, Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);
        if (!auctionItem.getSeller().getId().equals(userId)) {
            throw new CustomException(AUCTION_ITEM_NOT_USER);
        }
        return auctionItem;
    }

    private User validateUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return user;
    }
}
