package nbc.mushroom.domain.notice.service;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.like.repository.AuctionItemLikeRepository;
import nbc.mushroom.domain.notice.dto.NoticeRes;
import nbc.mushroom.domain.notice.entity.Notice;
import nbc.mushroom.domain.notice.entity.NoticeType;
import nbc.mushroom.domain.notice.repository.NoticeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AuctionItemLikeRepository auctionItemLikeRepository;

    @Scheduled(cron = "0 */1 * * * *") // 현재는 1분마다 돌아갑니다.
    public void createNoticeStartTime() {
        log.info("::::Create Notice Start Time::::");
        // 현재 시간
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림
        // 현재 시간에 10분 추가
        LocalDateTime nowPlus10 = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            .plusMinutes(10);
        // findNoticeInfoForLike 매개변수로 경매 물품의 경매 시작 시간 비교군 now, nowPlus
        List<NoticeRes> noticeResList = auctionItemLikeRepository.findNoticeInfoOfStartByAuctionItemLike(
            now,
            nowPlus10);
        for (NoticeRes noticeRes : noticeResList) {
            noticeRepository.save(Notice.builder()
                .auctionItem(noticeRes.auctionItem())
                .user(noticeRes.user())
                .auctionItemLike(noticeRes.auctionItemLike())
                .noticeType(NoticeType.START_TIME)
                .build());
        }
    }

    @Scheduled(cron = "0 */1 * * * *") // 현재는 1분마다 돌아갑니다.
    public void createNoticeEndTime() {
        log.info("::::Create Notice End Time::::");
        // 현재 시간
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림
        // 현재 시간에 10분 추가
        LocalDateTime nowPlus10 = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            .plusMinutes(10);
        // findNoticeInfoForLike 매개변수로 경매 물품의 경매 시작 시간 비교군 now, nowPlus
        List<NoticeRes> noticeResList = auctionItemLikeRepository.findNoticeInfoOfEndByAuctionItemLike(
            now,
            nowPlus10);
        for (NoticeRes noticeRes : noticeResList) {
            noticeRepository.save(Notice.builder()
                .auctionItem(noticeRes.auctionItem())
                .user(noticeRes.user())
                .auctionItemLike(noticeRes.auctionItemLike())
                .noticeType(NoticeType.END_TIME)
                .build());
        }
    }
}
