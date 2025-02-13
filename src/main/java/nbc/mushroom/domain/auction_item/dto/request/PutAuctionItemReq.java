package nbc.mushroom.domain.auction_item.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import org.springframework.web.multipart.MultipartFile;

public record PutAuctionItemReq(
    @NotBlank(message = "경매 물품 명은 필수이며, 공백일 수 없습니다.")
    String name,

    String description,

    @NotNull(message = "경매 물품 사이즈는 필수입니다.")
    AuctionItemSize auctionItemSize,
    @NotNull(message = "경매 물품 카테고리는 필수입니다.")
    AuctionItemCategory auctionItemCategory,
    @NotBlank(message = "경매 물품 브랜드 명은 필수이며, 공백일 수 없습니다.")
    String brand,
    @NotNull(message = "경매 물품의 입찰 시작 금액은 필수입니다.")
    Long startPrice,
    @NotNull(message = "경매 물품의 입찰 시작 시간 설정은 필수입니다.")
    LocalDateTime startTime,
    @NotNull(message = "경매 물품의 입찰 종료 시간 설정은 필수입니다.")
    LocalDateTime endTime,

    MultipartFile image
) {

}
