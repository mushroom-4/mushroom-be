package nbc.mushroom.domain.review.repository;

import java.util.List;
import nbc.mushroom.domain.review.entity.Review;

public interface ReviewRepositoryCustom {

    List<Review> findAllBySellerId(Long sellerId);

    Review findByBidIdAndUserId(Long bidId, Long id);
}
