package nbc.mushroom.domain.auction_item.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_NOT_USER;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.dto.request.CreateAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.request.PutAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemBidInfoRes;
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
import org.springframework.cache.annotation.Cacheable;
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
    private final ConcurrentHashMap<String, Integer> popularKeywordsMap = new ConcurrentHashMap<>();

    // 경매 물품 키워드 검색(조회)
    public Page<SearchAuctionItemRes> searchKeywordAuctionItems(String sort,
        String sortOrder, String keyword, String brand, AuctionItemCategory category,
        AuctionItemSize size, LocalDateTime startDate, LocalDateTime endDate, Long minPrice,
        Long maxPrice, Pageable pageable) {

        if (keyword != null && !keyword.isEmpty()) {
            savePopularKeywords(keyword);
        }

        return auctionItemRepository.findAuctionItemsByKeywordAndFiltering(
            sort, sortOrder, keyword, brand, category, size, startDate, endDate, minPrice, maxPrice,
            pageable);
    }

    // 경매 물품 단건 조회
    public SearchAuctionItemRes searchAuctionItem(long auctionItemId) {
        AuctionItem searchAuctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        return SearchAuctionItemRes.from(searchAuctionItem);
    }

    // 경매 물품 최대 입찰가 조회
    public SearchAuctionItemBidRes getAuctionItemWithMaxBid(long auctionItemId) {
        AuctionItem searchAuctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);
        if (bidRepository.existsBidByAuctionItem(searchAuctionItem)) {
            AuctionItemBidInfoRes auctionItemBidInfoRes =
                bidRepository.auctionItemBidInfoFind(auctionItemId);

            return SearchAuctionItemBidRes.from(searchAuctionItem, auctionItemBidInfoRes);
        }

        return SearchAuctionItemBidRes.from(searchAuctionItem, null);
    }

    // 경매 물품 목록 전체 조회
    public Page<SearchAuctionItemRes> findAllAuctionItems(Pageable pageable) {
        return auctionItemRepository.findAllAuctionItems(pageable);
    }

    // 경매 물품 생성
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

    // 경매 물품 수정
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

    // 경매 물품 삭제
    @Transactional
    public void softDeleteAuctionItem(Long userId, Long auctionItemId) {

        AuctionItem auctionItem = validateItemById(userId, auctionItemId);

        validateUserById(userId);

        auctionItem.softDelete();
    }

    public Boolean hasAuctionItem(Long auctionItemId) {
        return auctionItemRepository.existsById(auctionItemId);
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

    // 인기 검색어
    @Cacheable(value = "popularKeywords", key = "'top10'")
    public List<String> searchPopularKeywords() {
        return popularKeywordsMap.entrySet().stream()
            .sorted((k1, k2) -> k2.getValue().compareTo(k1.getValue()))// 검색 횟수 기준 내림차순 정렬
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    // 인기 검색어 저장
    public void savePopularKeywords(String keyword) {
        popularKeywordsMap.merge(keyword, 1, Integer::sum);
        log.info("인기 검색어 저장 - {} (현재 검색 횟수: {})", keyword, popularKeywordsMap.get(keyword));
    }

    // 검색어 캐싱 내역 가시화 로직
    public void printPopularKeywordsCacheContents() {
        log.info(":::현재 인기 검색어 캐시 내용:::");

        if (popularKeywordsMap.isEmpty()) {
            log.info("현재 저장된 검색어 데이터가 없습니다.");
            return;
        }

        popularKeywordsMap.entrySet().stream()
            .sorted((k1, k2) -> k2.getValue().compareTo(k1.getValue()))
            .limit(10)
            .forEach(entry -> log.info("  - 키워드: {}, 검색 횟수: {}", entry.getKey(), entry.getValue()));
    }
}
