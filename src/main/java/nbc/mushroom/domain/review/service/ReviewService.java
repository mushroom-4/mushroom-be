package nbc.mushroom.domain.review.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.review.dto.request.CreateSellerReviewReq;
import nbc.mushroom.domain.review.dto.response.CreateSellerReviewRes;
import nbc.mushroom.domain.review.dto.response.SearchSellerReviewDetailRes;
import nbc.mushroom.domain.review.dto.response.SearchSellerReviewRes;
import nbc.mushroom.domain.review.entity.Review;
import nbc.mushroom.domain.review.repository.ReviewRepository;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final BidRepository bidRepository;
    private final ReviewRepository reviewRepository;

    // 리뷰 생성
    @Transactional
    public CreateSellerReviewRes createReview(
        User loginUser,
        Long bidId,
        CreateSellerReviewReq createSellerReviewReq
    ) {

        Boolean existingReview = reviewRepository.existsByBidId(bidId);
        if (existingReview) {
            throw new CustomException(
                ExceptionType.REVIEW_ALREADY_EXISTS); // 구매자가 해당 판매자에 대해 이미 리뷰 작성한 경우 중복 불가
        }

        Bid bid = bidRepository.findById(bidId)
            .orElseThrow(() -> new CustomException(ExceptionType.BID_NOT_FOUND));

        if (bid.getBiddingStatus() != BiddingStatus.PAYMENT_COMPLETED) {
            throw new CustomException(ExceptionType.BID_NO_PAYMENT); // 낙찰 + 결제완료인 입찰자만 리뷰 작성에 해당
        }

        if (!bid.getBidder().getId().equals(loginUser.getId())) {
            throw new CustomException(ExceptionType.INVALID_REVIEW_USER);
        }

        Review review = Review.builder()
            .bid(bid)                  // 결제 완료 상태인 입찰
            .score(createSellerReviewReq.score())
            .content(createSellerReviewReq.content())
            .build();

        reviewRepository.save(review);

        return CreateSellerReviewRes.from(review);
    }

    // 리뷰 조회 (판매자 한명의 리뷰 리스트)
    public SearchSellerReviewRes searchReviewsBySeller(Long sellerId) {

        List<Review> reviews = reviewRepository.findAllBySellerId(sellerId);

        // 평균 점수 계산
        Double averageScore = reviews.stream()
            .mapToInt(Review::getScore)
            .average()
            .orElse(0.0);

        // 리뷰룰 DTO로 변환
        List<SearchSellerReviewDetailRes> reviewDetails = reviews.stream()
            .map(SearchSellerReviewDetailRes::from)
            .toList();

        // 평균 점수와 리뷰 세부정보를 포함한 결과 반환
        return new SearchSellerReviewRes(averageScore, reviewDetails);
    }
}

