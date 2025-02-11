package nbc.mushroom.domain.bid.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.service.BidService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/{productId}/bids")
public class BidControllerV1 {

    private final BidService bidService;
}
