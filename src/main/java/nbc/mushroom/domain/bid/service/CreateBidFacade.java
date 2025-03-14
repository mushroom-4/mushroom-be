package nbc.mushroom.domain.bid.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.dto.request.CreateBidReq;
import nbc.mushroom.domain.bid.dto.response.CreateBidRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.chat.service.ChatService;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateBidFacade {

    private final CreateBidService createBidService;
    private final ChatService chatService;

    public CreateBidRes createOrUpdateBid(
        User bidder,
        Long auctionItemId,
        CreateBidReq createBidReq
    ) {
        Bid newBid = createBidService.createOrUpdateBid(bidder, auctionItemId, createBidReq);
        chatService.sendBidAnnouncementMessage(
            newBid.getAuctionItem().getId(),
            newBid.getBidder(),
            newBid.getBiddingPrice()
        );

        return CreateBidRes.from(newBid);
    }
}
