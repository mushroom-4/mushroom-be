package nbc.mushroom.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.review.dto.request.CreateReviewReq;
import nbc.mushroom.domain.review.dto.response.CreateReviewRes;
import nbc.mushroom.domain.review.service.ReviewService;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{sellerId}/reviews")
    public ResponseEntity<ApiResponse<CreateReviewRes>> createReview(
        @Auth AuthUser authUser,
        @PathVariable Long sellerId,
        @Valid @RequestBody CreateReviewReq createReviewReq
    ) {
        CreateReviewRes createReviewRes = reviewService.createReview(
            User.fromAuthUser(authUser),
            sellerId,
            createReviewReq);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body((ApiResponse.success("리뷰가 정상적으로 생성되었습니다.", createReviewRes)));

    }
}

