package nbc.mushroom.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.user.service.UserBidService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/bids")
@RequiredArgsConstructor
public class UserBidControllerV1 {

    private final UserBidService userBidService;

}
