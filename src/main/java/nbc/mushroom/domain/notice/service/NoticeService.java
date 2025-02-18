package nbc.mushroom.domain.notice.service;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.like.repository.LikeRepository;
import nbc.mushroom.domain.notice.dto.NoticeRes;
import nbc.mushroom.domain.notice.entity.Notice;
import nbc.mushroom.domain.notice.repository.NoticeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final LikeRepository likeRepository;

    @Scheduled(cron = "0 */1 * * * *")
    public void createNoticeStartTime() {
        log.info("::::Create Notice Start Time::::");
        // 현재 시간
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림
        // 현재 시간에 10분 추가
        LocalDateTime nowPlus10 = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            .plusMinutes(10);
        // findNoticeInfoForLike 매개변수로 경매 물품의 경매 시작 시간 비교군 now, nowPlus
        List<NoticeRes> noticeResList = likeRepository.findNoticeInfoForLike(now, nowPlus10);
        for (NoticeRes noticeRes : noticeResList) {
            noticeRepository.save(Notice.builder()
                .auctionItem(noticeRes.auctionItem())
                .user(noticeRes.user())
                .like(noticeRes.like())
                .build());
        }
    }
}
