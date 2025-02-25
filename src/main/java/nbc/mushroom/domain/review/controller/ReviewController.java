package nbc.mushroom.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.review.dto.request.CreateSellerReviewReq;
import nbc.mushroom.domain.review.dto.response.CreateSellerReviewRes;
import nbc.mushroom.domain.review.dto.response.SearchSellerReviewRes;
import nbc.mushroom.domain.review.service.ReviewService;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/bids/{bidId}/reviews")
    public ResponseEntity<ApiResponse<CreateSellerReviewRes>> createReview(
        @Auth AuthUser authUser,
        @PathVariable Long bidId,
        @Valid @RequestBody CreateSellerReviewReq createSellerReviewReq
    ) {
        CreateSellerReviewRes createSellerReviewRes = reviewService.createReview(
            User.fromAuthUser(authUser),
            bidId,
            createSellerReviewReq);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body((ApiResponse.success("리뷰가 정상적으로 생성되었습니다.", createSellerReviewRes)));

    }

    @GetMapping("/users/{sellerId}/reviews")
    public ResponseEntity<ApiResponse<SearchSellerReviewRes>> searchReviews(
        @PathVariable Long sellerId
    ) {
        SearchSellerReviewRes searchSellerRes = reviewService.searchReviewsBySeller(sellerId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("판매자 리뷰 조회가 성공적으로 이루어졌습니다.", searchSellerRes));
    }
}

