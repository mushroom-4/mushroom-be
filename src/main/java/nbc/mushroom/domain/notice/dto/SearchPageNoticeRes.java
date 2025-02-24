package nbc.mushroom.domain.notice.dto;

public record SearchPageNoticeRes(
    String message,
    Long noticeId
) {

    public static SearchPageNoticeRes from(
        String message,
        Long noticeId
    ) {
        return new SearchPageNoticeRes(message, noticeId);
    }
}
