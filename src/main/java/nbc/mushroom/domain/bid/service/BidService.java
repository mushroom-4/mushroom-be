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
        CreateBidReq createBidReq) {
        Product findProduct = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(
                ExceptionType.PRODUCT_NOT_FOUND)); // 이 동작을 bid서비스에서 보여주고 싶지 않은데.. Product Repository에 디폴트 메서드 또는 Bid로 반환하는 메서드 생기면 변경하기로

        Bid findBid = bidRepository.findBidByUserAndProduct(loginUser, findProduct)
            .orElseGet(() -> createBid(loginUser, findProduct, createBidReq.biddingPrice()));

        findBid.updateBiddingPrice(createBidReq.biddingPrice());

        return CreateBidRes.from(findBid);
    }

    @Transactional(readOnly = false)
    public Bid createBid(User bidder, Product product, Long biddingPrice) {
        Bid bid = Bid.builder()
            .product(product)
            .biddingPrice(biddingPrice)
            .bidder(bidder)
            .build();

        return bidRepository.save(bid);
    }
}
