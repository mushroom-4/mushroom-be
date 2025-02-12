package nbc.mushroom.domain.common.dto;


import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

public record CreateProductReq(String name, String description,
                               String productSize, String productCategory,
                               String brand, Long startPrice,
                               LocalDateTime startTime, LocalDateTime endTime,
                               MultipartFile image
) {

}
