package nbc.mushroom.domain.notice.repository;

import java.util.List;
import nbc.mushroom.domain.notice.entity.Notice;
import nbc.mushroom.domain.user.entity.User;

public interface NoticeRepositoryCustom {

    List<Notice> findAllNoticeResByUser(User user);
}
