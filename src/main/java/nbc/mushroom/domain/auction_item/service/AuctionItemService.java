package nbc.mushroom.domain.auction_item.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_NOT_USER;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.dto.request.CreateAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.request.PutAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemBidInfo;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemBidRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.repository.BidRepository;
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
    private final BidRepository bidRepository;
//    private final CacheManager cacheManager;
//    private final ConcurrentHashMap<String, Integer> popularKeywordsMap = new ConcurrentHashMap<>();

    public Page<SearchAuctionItemRes> searchKeywordAuctionItems(String sort,
        String sortOrder, String keyword, String brand, AuctionItemCategory category,
        AuctionItemSize size, LocalDateTime startDate, LocalDateTime endDate, Long minPrice,
        Long maxPrice, Pageable pageable) {
        return auctionItemRepository.findAuctionItemsByKeywordAndFiltering(
            sort, sortOrder, keyword, brand, category, size, startDate, endDate, minPrice, maxPrice,
            pageable);
    }

    public SearchAuctionItemRes searchAuctionItem(long auctionItemId) {
        AuctionItem searchAuctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);
        return SearchAuctionItemRes.from(searchAuctionItem);
    }

    public SearchAuctionItemBidRes searchAuctionItemV2(long auctionItemId) {
        AuctionItem searchAuctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);
        if (bidRepository.existsBidByAuctionItem(searchAuctionItem)) {
            AuctionItemBidInfo auctionItemBidInfo =
                bidRepository.auctionItemBidInfoFind(auctionItemId);
            return SearchAuctionItemBidRes.from(searchAuctionItem, auctionItemBidInfo);
        }

        return SearchAuctionItemBidRes.from(searchAuctionItem, null);
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

//    // 인기 검색어 조회 // 차순위 개발
//    public List<String> getPopularKeywords() {
//        if (popularKeywordsMap.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        return getTopRankEntries().stream()
//            .map(Map.Entry::getKey)
//            .collect(Collectors.toList());
//    }
//
//    // 인기 검색어 추출 메서드 (상위 10개)
//    private List<Map.Entry<String, Integer>> getTopRankEntries() {
//        return popularKeywordsMap.entrySet().stream()
//            .sorted((k1, k2) -> k2.getValue().compareTo(k1.getValue()))
//            .limit(10)
//            .toList();
//    }
//
//    // 인메모리 캐시 가시화 로직
//    public void printCacheContents(String storedCache) {
//        Cache cache = cacheManager.getCache(storedCache);
//        if (cache != null) {
//            log.info("현재 '{}' 저장된 캐시 : ", storedCache);
//        } else {
//            log.warn("'{}' 캐시가 존재하지 않습니다.", storedCache);
//        }
//    }
}
