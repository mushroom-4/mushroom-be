package nbc.mushroom.domain.review.dto.response;

import nbc.mushroom.domain.review.entity.Review;

public record CreateReviewRes(
    int score,
    String content
) {

    public static CreateReviewRes from(Review review) {
        return new CreateReviewRes(
            review.getScore(),
            review.getContent()
        );
    }
}