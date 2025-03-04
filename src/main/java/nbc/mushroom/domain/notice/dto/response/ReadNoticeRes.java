package nbc.mushroom.domain.notice.dto.response;

import nbc.mushroom.domain.notice.entity.Notice;

public record ReadNoticeRes(
    String message,
    Long noticeId,
    Long auctionItemId
) {

    public static ReadNoticeRes from(Notice notice) {
        return new ReadNoticeRes(
            notice.createMessage(),
            notice.getId(),
            notice.getAuctionItem().getId()
        );
    }
}
