package nbc.mushroom.domain.review.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.review.dto.request.CreateSellerReviewReq;
import nbc.mushroom.domain.review.dto.response.CreateSellerReviewRes;
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
        Long sellerId,
        CreateSellerReviewReq createSellerReviewReq
    ) {

        Review existingReview = reviewRepository.findByBidderAndAuctionItem_Seller(
            loginUser, sellerId);
        if (existingReview != null) {
            throw new CustomException(
                ExceptionType.REVIEW_ALREADY_EXISTS); // 구매자가 해당 판매자에 대해 이미 리뷰 작성한 경우 중복 불가
        }

        if (loginUser.getId().equals(sellerId)) {
            throw new CustomException(ExceptionType.SELLER_CANNOT_REVIEW); // 판매자 본인의 리뷰 불가
        }

        BiddingStatus paymentCompleted = BiddingStatus.PAYMENT_COMPLETED;
        Bid winningBid = bidRepository.findByAuctionItemAndBidderAndBiddingStatus(
            sellerId, loginUser, paymentCompleted);

        if (winningBid == null) {
            throw new CustomException(ExceptionType.BID_NO_PAYMENT); // 낙찰 + 결제완료인 입찰자만 리뷰 작성에 해당
        }

        AuctionItem auctionItem = winningBid.getAuctionItem();

        Review review = Review.builder()
            .bid(winningBid)                  // 결제 완료 입찰인 상태
            .score(createSellerReviewReq.score())
            .content(createSellerReviewReq.content())
            .build();

        reviewRepository.save(review);

        return CreateSellerReviewRes.from(review);
    }
}

