package nbc.mushroom.domain.user.service;

import static nbc.mushroom.domain.notice.entity.NoticeType.END_TIME;
import static nbc.mushroom.domain.notice.entity.NoticeType.START_TIME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.notice.dto.SearchNoticeEndTypeRes;
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
public class UserNoticeUpgradeService {

    private final NoticeRepository noticeRepository;
    private final BidRepository bidRepository;

    public Page<SearchPageNoticeRes> searchUserNotice(User user) {
        // 유저가 가지는 공지 List
        List<SearchNoticeRes> noticeList = noticeRepository.findNoticeTypeInfoByNoticeList(
            user);

        // 정렬 응답 객체를 담는 곳
        List<SearchPageNoticeRes> orderSearchPageNoticeResList = new ArrayList<>();
        String message = "";

        // 유저가 가지는 공지 List : noticeTypeIfoByNoticeList 가 있을 때
        if (!noticeList.isEmpty()) {

            // 공지 리스트 중 공지 타입이 START_TIME 인 것...경매 물품 아이디를 가지고 온다.
            List<SearchNoticeRes> startTypeNoticeList = noticeList.stream()
                .filter(searchNoticeRes -> searchNoticeRes.notice().getNoticeType()
                    == START_TIME) // NoticeType.START_TIME만 필터링
                .collect(Collectors.toList());

            // 공지 리스트 중 공지 타입이 END_TIME 인 것...경매 물품 아이디를 가지고 온다.
            List<SearchNoticeRes> endTypeNoticeList = noticeList.stream()
                .filter(searchNoticeRes -> searchNoticeRes.notice().getNoticeType()
                    == END_TIME) // NoticeType.END_TIME만 필터링
                .collect(Collectors.toList());

            // endTypeNoticeList 를 입찰 래포지토리에서 bid가 존재하면,
            // bid 정보를 BidInNoticeList 반환
            List<SearchNoticeEndTypeRes> BidInNoticeList = bidRepository.auctionItemBidInfoFindList(
                endTypeNoticeList);

            // 위에 걸로  endTypeNoticeList 에서 auctionItem_id 가 같은, bid 가 있는 BidNoticeList,
            // type 이 end 인건 이미 걸러진 상황

            List<SearchNoticeRes> bidAndEndTypeNoticeList = new ArrayList<>(); // end && 비드가 있는 넘

            List<SearchNoticeRes> nonBidAndEndTypeNoticeList = new ArrayList<>();

            // notice_id 로 정령 전 응답 객체 데이터
            List<SearchPageNoticeRes> nonOrderSearchPageNoticeResList = new ArrayList<>();

            // notice END_TYPE &&  bid 있는넘 이 있을 시
            if (!BidInNoticeList.isEmpty()) {

                for (int i = 0; i < BidInNoticeList.size(); i++) { // 1 6 9
                    Long auctionIdOfEndTypeAndBidNotice = BidInNoticeList.get(i)
                        .auctionId();

                    for (int j = 0; j < endTypeNoticeList.size(); j++) { // 1 2 3 6 9
                        if (endTypeNoticeList.get(j).auctionItem().getId()
                            == auctionIdOfEndTypeAndBidNotice) {
                            bidAndEndTypeNoticeList.add(endTypeNoticeList.get(j));

                            // 중복 방지를 위해
                            endTypeNoticeList.remove(j);
                        }
                    }
                }

                // bid 있는거 && END_TYPE 의 message 생성
                for (int i = 0; i < bidAndEndTypeNoticeList.size(); i++) {
                    Long maxPrice = BidInNoticeList.get(i).maxPrice();

                    message = bidAndEndTypeNoticeList.get(i).user().getNickname() + "님! 좋아요 하신 " +
                        bidAndEndTypeNoticeList.get(i).auctionItem().getName()
                        + "경매 물품이 10분 후 경매가  종료됩니다! "
                        + "현재 입찰 최고가는 " + +maxPrice + " 원으로 최고 입찰가입니다!";
                    nonOrderSearchPageNoticeResList.add(
                        SearchPageNoticeRes.from(message,
                            bidAndEndTypeNoticeList.get(i).notice().getId()));
                }
            }

            //if (!BidInNoticeList.isEmpty())  문 밖

            nonBidAndEndTypeNoticeList = endTypeNoticeList;

            // 비드가 없는 && END_TYPE 의 message 생성
            for (int i = 0; i < nonBidAndEndTypeNoticeList.size(); i++) {

                message = nonBidAndEndTypeNoticeList.get(i).user().getNickname() + "님! 좋아요 하신 " +
                    nonBidAndEndTypeNoticeList.get(i).auctionItem().getName()
                    + "경매 물품이 10분 후 경매가  종료됩니다! 입찰 시작가 " +
                    nonBidAndEndTypeNoticeList.get(i).auctionItem().getStartPrice() + "원 입니다!";
                nonOrderSearchPageNoticeResList.add(
                    SearchPageNoticeRes.from(message,
                        nonBidAndEndTypeNoticeList.get(i).notice().getId()));
            }

            // START_TYPE 의 message 생성
            for (int i = 0; i < startTypeNoticeList.size(); i++) {
                message = startTypeNoticeList.get(i).user().getNickname() + "님! 좋아요 하신 " +
                    startTypeNoticeList.get(i).auctionItem().getName()
                    + "경매 물품이 10분 후 경매가  시작됩니다! ";
                nonOrderSearchPageNoticeResList.add(
                    SearchPageNoticeRes.from(message, startTypeNoticeList.get(i).notice().getId()));
            }

            // notice.id 기준 내림 차순 정렬
            orderSearchPageNoticeResList =
                nonOrderSearchPageNoticeResList.stream()
                    .sorted(Comparator.comparing(
                        SearchPageNoticeRes::noticeId).reversed())
                    .collect(Collectors.toList());

        } else { // 유저가 가지는 공지 List : noticeTypeIfoByNoticeList 가 없을 때
            message = "고객님의 공지는 아직 없어요!";
            SearchPageNoticeRes nonOrderSearchPageNoticeResList = SearchPageNoticeRes.from(message);
            orderSearchPageNoticeResList = Collections.singletonList(
                nonOrderSearchPageNoticeResList);
        }
        int size = 10;  // 추후 삭제 로직에 필요
        int page = 0;

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();  // 현재 페이지의 첫 번째 항목 인덱스
        // end는 start + 페이지 크기(size)와 실제 데이터 크기 중 작은 값으로 제한
        int end = Math.min((start + pageable.getPageSize()),
            orderSearchPageNoticeResList.size());

        List<SearchPageNoticeRes> pagedList = orderSearchPageNoticeResList.subList(start, end);

        return new PageImpl<>(pagedList, pageable, orderSearchPageNoticeResList.size());
    }

}
