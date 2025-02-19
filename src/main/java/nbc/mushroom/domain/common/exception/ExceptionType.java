package nbc.mushroom.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {

    //Server
    SERVER_IMAGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SV01", "이미지 서버에 문제가 생겼습니다."),
    SERVER_PAYMENT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SV02", "결제가 정상적으로 되지 않았습니다."),

    //Auth
    AUTH_WRONG_USED(HttpStatus.INTERNAL_SERVER_ERROR, "A01", "@Auth와 AuthUser 타입은 함께 사용되어야 합니다."),
    AUTH_FAILED(HttpStatus.BAD_REQUEST, "A02", "이메일 또는 비밀번호가 일치하지 않습니다."),
    AUTH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "A03", "토큰을 찾을 수 없습니다."),

    //User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U01", "해당 사용자를 찾을 수 없습니다."),
    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "U02", "이메일이 중복됩니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "U03", "기존 비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME(HttpStatus.BAD_REQUEST, "U04", "새 비밀번호는 기존 비밀번호와 같을 수 없습니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "U05", "유효하지 않은 UserRole"),

    //AuctionItem
    AUCTION_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "AI01", "존재하지 않는 물품입니다."),
    AUCTION_ITEM_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "AI02", "경매가 진행 중인 물품이 아닙니다."),
    AUCTION_ITEM_NOT_USER(HttpStatus.FORBIDDEN, "AI03", "사용자 본인의 물품이어야 합니다."),
    REGISTERED_AUCTION_ITEMS_NOT_FOUND(HttpStatus.NOT_FOUND, "AI04", "등록된 경매 물품이 없습니다."),

    //AuctionItem Admin
    INVALID_AUCTION_ITEM_STATUS(HttpStatus.BAD_REQUEST, "AIA01", "잘못된 상태 변경 요청입니다."),
    AUCTION_ITEM_ALREADY_INSPECTED(HttpStatus.BAD_REQUEST, "AIA02", "이미 검수가 완료된 물품입니다."),


    // Bid
    SELF_BIDDING_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "B01", "본인 물품을 입찰할 수 없습니다."),
    INVALID_BIDDING_PRICE(HttpStatus.BAD_REQUEST, "B02", "입찰 금액은 현재 최고 입찰가 이상이어야 합니다."),
    INVALID_BID_STATUS(HttpStatus.BAD_REQUEST, "B03", "잘못된 입찰 상태 변경 요청입니다."),
    BID_NOT_FOUND(HttpStatus.NOT_FOUND, "B04", "해당 입찰 내역이 존재하지 않습니다."),
    BID_CANNOT_CANCEL_WITHIN_24HOURS(HttpStatus.BAD_REQUEST, "B05",
        "경매 종료 시간 24시간 이내에는 취소할 수 없습니다."),
    BID_CANCELLATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "B06",
        "입찰 취소 제한(3회)을 초과하여 더 이상 취소할 수 없습니다."),
    BID_CANNOT_CANCEL_NON_BIDDING(HttpStatus.BAD_REQUEST, "B07", "입찰이 진행 중일 때만 취소할 수 있습니다."),

    // Payment
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "P01", "결제 금액이 입찰 가격과 일치하지 않습니다."),

    // Like
    EXIST_LIKE_BY_AUCTION_ITEM(HttpStatus.BAD_REQUEST, "L01", "좋아요는 경매 물품 하나 당 한 번만 가능합니다."),
    NOT_SELF_LIKE(HttpStatus.BAD_REQUEST, "L02", "좋아요는 본인 경매 물품에 등록할 수 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "L03", "해당 경매 물품에 좋아요를 등록하지 않았습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
