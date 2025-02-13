package nbc.mushroom.domain.auction_item.entity;

public enum AuctionItemStatus {
    INSPECTING,          // 검수중
    REJECTED,            // 검수실패
    WAITING,             // 대기중
    PROGRESSING,         // 진행중
    COMPLETED            // 종료
}
