package nbc.mushroom.domain.admin.dto.request;

import java.time.LocalDateTime;

public record UpdateAuctionItemTimeReq(LocalDateTime startTime, LocalDateTime endTime) {

}
