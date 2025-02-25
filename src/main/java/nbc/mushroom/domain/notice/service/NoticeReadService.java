package nbc.mushroom.domain.notice.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.notice.dto.ReadNoticeRes;
import nbc.mushroom.domain.notice.entity.Notice;
import nbc.mushroom.domain.notice.repository.NoticeRepository;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoticeReadService {

    private final NoticeRepository noticeRepository;

    public List<ReadNoticeRes> searchUserNotice(User user) {
        List<Notice> noticeList = noticeRepository.findAllNoticeResByUser(user);

        return noticeList.stream().map(ReadNoticeRes::from).toList();
    }
}
