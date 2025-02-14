package nbc.mushroom.domain.user.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.REGISTERED_AUCTION_ITEMS_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionItemRegisterServiceV1 {

    private final AuctionItemRepository auctionItemRepository;

    public Page<AuctionItemRes> searchRegisteredAuctionItems(Long userId, Pageable pageable) {
        Page<AuctionItem> registeredAuctionItems = auctionItemRepository.findRegisteredAuctionItemsByUserId(
            userId,
            pageable);

        if (registeredAuctionItems.isEmpty()) {
            throw new CustomException(REGISTERED_AUCTION_ITEMS_NOT_FOUND);
        }

        return registeredAuctionItems.map(AuctionItemRes::from);
    }
}
