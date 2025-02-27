package nbc.mushroom.domain.auction_item.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_DELETE_NOT_ALLOW;
import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_NOT_USER;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.dto.request.CreateAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.request.UpdateAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemBidInfoRes;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemBidRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.image.ImageUtil;
import nbc.mushroom.domain.review.entity.Review;
import nbc.mushroom.domain.review.repository.ReviewRepository;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BidRepository bidRepository;
    private final ImageUtil imageUtil;
    private final RedisTemplate<String, String> redisCacheTemplate;
    private final static String POPULAR_KEYWORDS_KEY = "popularKeywords";


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

    // 경매 물품 상세 조회 (최고 입찰가, 판매자 정보)
    public SearchAuctionItemBidRes getAuctionItem(long auctionItemId) {
        AuctionItem searchAuctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        List<Review> reviews = reviewRepository.findAllBySellerId(
            searchAuctionItem.getSeller().getId());

        if (bidRepository.existsBidByAuctionItem(searchAuctionItem)) {
            AuctionItemBidInfoRes auctionItemBidInfoRes =
                bidRepository.auctionItemBidInfoFind(auctionItemId);

            return SearchAuctionItemBidRes.from(searchAuctionItem, auctionItemBidInfoRes, reviews);
        }

        return SearchAuctionItemBidRes.from(searchAuctionItem, null, reviews);
    }

    // 경매 물품 키워드 검색(조회)
    public Page<SearchAuctionItemRes> getFilteredAuctionItems(String sort,
        String sortOrder, String keyword, String brand, AuctionItemCategory category,
        AuctionItemSize size, LocalDateTime startDate, LocalDateTime endDate, Long minPrice,
        Long maxPrice, AuctionItemStatus status, Pageable pageable) {

        if (keyword != null && !keyword.isEmpty()) {
            savePopularKeywords(keyword);
        }

        return auctionItemRepository.findAuctionItemsByKeywordAndFiltering(
            sort, sortOrder, keyword, brand, category, size, startDate, endDate, minPrice, maxPrice,
            status, pageable);
    }

    // 경매 물품 수정
    @Transactional
    public AuctionItemRes updateAuctionItem(Long userId, Long auctionItemId,
        UpdateAuctionItemReq updateAuctionItemReq) {

        AuctionItem auctionItem = validateItemById(userId, auctionItemId);

        auctionItemRepository.findAuctionItemById(auctionItemId);

        User user = validateUserById(userId);

        if (updateAuctionItemReq.image() != null) {
            imageUtil.delete(auctionItem.getImageUrl());
        }

        String fileName = imageUtil.upload(updateAuctionItemReq.image());
        String updateImageUrl = imageUtil.getImageUrl(fileName);

        AuctionItem updateAuctionItem = AuctionItem.builder()
            .id(auctionItemId)
            .seller(user)
            .name(updateAuctionItemReq.name())
            .description(updateAuctionItemReq.description())
            .brand(updateAuctionItemReq.brand())
            .imageUrl(fileName)
            .size(updateAuctionItemReq.auctionItemSize())
            .category(updateAuctionItemReq.auctionItemCategory())
            .startPrice(updateAuctionItemReq.startPrice())
            .startTime(updateAuctionItemReq.startTime())
            .endTime(updateAuctionItemReq.endTime())
            .build();

        auctionItemRepository.save(updateAuctionItem);

        return AuctionItemRes.from(updateAuctionItem, updateImageUrl);
    }

    // 경매 물품 삭제
    @Transactional
    public void deleteAuctionItem(Long userId, Long auctionItemId) {

        AuctionItem auctionItem = validateItemById(userId, auctionItemId);

        auctionItem.delete();
    }

    public Boolean hasAuctionItem(Long auctionItemId) {
        return auctionItemRepository.existsById(auctionItemId);
    }

    // 인기 검색어 TODO 캐시 만료 정책을 어떻게 할것인가?
    @Cacheable(value = "popularKeywords", key = "'top10'")
    public List<String> getPopularKeywords() {

        Set<String> keywords = redisCacheTemplate.opsForZSet()
            .reverseRange(POPULAR_KEYWORDS_KEY, 0, 9);

        return keywords != null ? new ArrayList<>(keywords) : new ArrayList<>();
    }

    // 인기 검색어 저장
    public void savePopularKeywords(String keyword) {
        redisCacheTemplate.opsForZSet().incrementScore(POPULAR_KEYWORDS_KEY, keyword, 1);

        redisCacheTemplate.expire(POPULAR_KEYWORDS_KEY,
            Duration.ofMinutes(10)); // todo 테스트 후 만료 정책 변경

        log.info("인기 검색어 저장 - {} (현재 검색 횟수: {})", keyword,
            redisCacheTemplate.opsForZSet().score(POPULAR_KEYWORDS_KEY, keyword));
    }

    // 검색어 캐싱 내역 가시화 로직
    public void printPopularKeywordsCacheContents() {
        log.info(":::현재 인기 검색어 캐시 내용:::");

        Set<ZSetOperations.TypedTuple<String>> entries = redisCacheTemplate.opsForZSet()
            .reverseRangeWithScores(POPULAR_KEYWORDS_KEY, 0, 9);

        if (entries == null || entries.isEmpty()) {
            log.info("현재 저장된 검색어 데이터가 없습니다.");
            return;
        }

        entries.forEach(
            entry -> log.info("  - 키워드: {}, 검색 횟수: {}", entry.getValue(), entry.getScore()));
    }

    private AuctionItem validateItemById(Long userId, Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);
        if (!auctionItem.getSeller().getId().equals(userId)) {
            throw new CustomException(AUCTION_ITEM_NOT_USER);
        }
        if (auctionItem.getStatus() == AuctionItemStatus.PROGRESSING
            || auctionItem.getStatus() == AuctionItemStatus.COMPLETED) {
            throw new CustomException(AUCTION_ITEM_DELETE_NOT_ALLOW);
        }
        return auctionItem;
    }

    private User validateUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return user;
    }
}
