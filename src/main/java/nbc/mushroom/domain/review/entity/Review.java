package nbc.mushroom.domain.review.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.common.entity.Timestamped;

@Getter
@Entity
@Table(name = "`review`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", nullable = false)
    private Bid bid;

    @Column(name = "score", nullable = false)
    private int score; // int vs short 찾아보기

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public Review(Long id, Bid bid, int score, String content) {
        this.id = id;
        this.bid = bid;
        this.score = score;
        this.content = content;
    }

}
