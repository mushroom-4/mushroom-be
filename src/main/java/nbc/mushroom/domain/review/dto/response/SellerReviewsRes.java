package nbc.mushroom.domain.review.dto.response;

import java.util.List;

public record SellerReviewsRes(
    Double averageScore,
    List<SellerReviewDetailRes> reviews
) {

}

