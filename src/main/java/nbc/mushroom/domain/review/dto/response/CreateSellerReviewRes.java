package nbc.mushroom.domain.review.dto.response;

import nbc.mushroom.domain.review.entity.Review;

public record CreateSellerReviewRes(
    int score,
    String content
) {

    public static CreateSellerReviewRes from(Review review) {
        return new CreateSellerReviewRes(
            review.getScore(),
            review.getContent()
        );
    }
}