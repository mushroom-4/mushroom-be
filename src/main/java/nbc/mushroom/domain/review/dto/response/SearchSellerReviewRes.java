package nbc.mushroom.domain.review.dto.response;

import java.util.List;

public record SearchSellerReviewRes(
    Double averageScore,
    List<SearchSellerReviewDetailRes> reviews
) {

}

