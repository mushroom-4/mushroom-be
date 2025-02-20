package nbc.mushroom.domain.user.service;

import static nbc.mushroom.domain.notice.entity.NoticeType.END_TIME;
import static nbc.mushroom.domain.notice.entity.NoticeType.START_TIME;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.notice.dto.SearchNoticeRes;
import nbc.mushroom.domain.notice.dto.SearchPageNoticeRes;
import nbc.mushroom.domain.notice.repository.NoticeRepository;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserNoticeService {

    private final NoticeRepository noticeRepository;

    public Page<SearchPageNoticeRes> searchUserNotice(User user, int page) {
        // 유저가 가지는 공지 List
        List<SearchNoticeRes> SearchNoticeResList = noticeRepository.findNoticeTypeInfoByNoticeList(
            user);

        String message = "";

        List<SearchPageNoticeRes> searchPageNoticeResList = new ArrayList<>();
        for (SearchNoticeRes searchNoticeRes : SearchNoticeResList) { // start 로 받아서 처리
            // 시작 시간 일때
            // notice.user.name 님 좋아요 하신 notice.auctionItem.name 경매 상품이 10분 후 경매가 진행됩니다.
            if (searchNoticeRes.notice().getNoticeType() == START_TIME) {
                message = searchNoticeRes.user().getNickname() + "님! 좋아요 하신 " +
                    searchNoticeRes.auctionItem().getName() + "경매 물품이 10분 후 경매가 진행됩니다!";
                searchPageNoticeResList.add(
                    SearchPageNoticeRes.from(message, searchNoticeRes.notice().getId()));
            }
            //종료 시간 일때
            if (searchNoticeRes.notice().getNoticeType() == END_TIME) {
                message = searchNoticeRes.user().getNickname() + "님! 좋아요 하신 " +
                    searchNoticeRes.auctionItem().getName() + "경매 물품이 10분 후 경매가  종료됩니다!";
                searchPageNoticeResList.add(
                    SearchPageNoticeRes.from(message, searchNoticeRes.notice().getId()));
            }
        }

        int size = 10;

        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), searchPageNoticeResList.size());

        List<SearchPageNoticeRes> pagedList = searchPageNoticeResList.subList(start, end);

        return new PageImpl<>(pagedList, pageable, searchPageNoticeResList.size());
    }
}
