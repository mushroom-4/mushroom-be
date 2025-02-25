package nbc.mushroom.domain.review.repository;

import java.util.List;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Boolean existsByBidId(Long bidId);

    List<Review> bid(Bid bid);
}
