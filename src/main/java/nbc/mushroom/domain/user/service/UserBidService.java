package nbc.mushroom.domain.user.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.user.dto.response.UserBidRes;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBidService {

    private final BidRepository bidRepository;

    public Page<UserBidRes> getUserBidHistory(User loginUser, Pageable pageable) {
        Page<Bid> bidPage = bidRepository.findBidsByUser(loginUser, pageable);

        return bidPage.map(bid ->
            UserBidRes.from(bid, SearchAuctionItemRes.from(bid.getAuctionItem())));
    }
}