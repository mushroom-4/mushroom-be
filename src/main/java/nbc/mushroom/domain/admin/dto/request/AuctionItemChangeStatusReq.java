package nbc.mushroom.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuctionItemChangeStatusReq(
    @NotBlank
    @Pattern(
        regexp = "^(?i)(approve|reject)$", // (?i) 대소문자 구분 없이 허용
        message = "action은 'approve' 또는 'reject'만 가능합니다."
    )
    String action
) {

}

