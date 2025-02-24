package nbc.mushroom.domain.like.repository;

import nbc.mushroom.domain.like.entity.AuctionItemLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemLikeRepository extends JpaRepository<AuctionItemLike, Long>,
    AuctionItemLikeRepositoryCustom {

}
