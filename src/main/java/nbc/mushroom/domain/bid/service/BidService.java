package nbc.mushroom.domain.bid.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.dto.request.CreateBidReq;
import nbc.mushroom.domain.bid.dto.response.CreateBidRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.repository.ProductRepository;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = false)
    public CreateBidRes createOrUpdateBid(User loginUser, Long productId,
        // 메서드 명 updateBid로 바꾸고 find 관련된 로직 다 추출해버릴까..
        CreateBidReq createBidReq) {

        Product findProduct = productRepository.findProductById(productId);

        if (loginUser == findProduct.getSeller()) {
            throw new CustomException(ExceptionType.SELF_BIDDING_NOT_ALLOWED);
        }

        if (findProduct.getStartPrice() > createBidReq.biddingPrice()) {
            throw new CustomException(ExceptionType.INVALID_BIDDING_PRICE);
        }

        Bid findBid = bidRepository.findBidByUserAndProduct(loginUser, findProduct)
            .orElseGet(() -> createBid(loginUser, findProduct, createBidReq.biddingPrice())
            ); // 좀 더 생각.. Bid 생성까지 Repository에서 처리하는건 아닌듯..

        if (!createBidReq.biddingPrice().equals(findBid.getBiddingPrice())) {
            findBid.updateBiddingPrice(createBidReq.biddingPrice());
        }

        return CreateBidRes.from(findBid);
    }

    private Bid createBid(User bidder, Product product, Long biddingPrice) {
        Bid bid = Bid.builder()
            .product(product)
            .biddingPrice(biddingPrice)
            .bidder(bidder)
            .build();

        return bidRepository.save(bid);
    }
}
