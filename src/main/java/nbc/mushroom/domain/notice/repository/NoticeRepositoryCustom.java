package nbc.mushroom.domain.notice.repository;

import java.util.List;
import nbc.mushroom.domain.notice.dto.SearchNoticeRes;
import nbc.mushroom.domain.user.entity.User;

public interface NoticeRepositoryCustom {

    List<SearchNoticeRes> findNoticeTypeIfoByNoticeList(User user);
}
