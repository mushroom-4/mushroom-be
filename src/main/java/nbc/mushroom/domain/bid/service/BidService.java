package nbc.mushroom.domain.bid.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.repository.BidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
}
