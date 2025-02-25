package nbc.mushroom.domain.notice.dto;

public record ReadNoticeRes(
    String message,
    Long noticeId
) {

    public static ReadNoticeRes from(
        String message,
        Long noticeId
    ) {
        return new ReadNoticeRes(message, noticeId);
    }
}
