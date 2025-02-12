package nbc.mushroom.domain.common.dto;


import java.time.LocalDateTime;

public record CreateProductReq(String name, String description,
                               String productSize, String productCategory,
                               String brand, Long startPrice,
                               LocalDateTime startTime, LocalDateTime endTime
) {

}
