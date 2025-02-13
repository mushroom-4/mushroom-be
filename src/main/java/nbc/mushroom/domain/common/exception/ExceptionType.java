package nbc.mushroom.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {

    //Server
    SERVER_IMAGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SV01", "이미지 서버에 문제가 생겼습니다."),

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

    //Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P01", "존재하지 않는 상품입니다."),
    PRODUCT_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "P02", "경매가 진행 중인 상품이 아닙니다."),
    PRODUCT_NOT_USER(HttpStatus.FORBIDDEN, "P03", "사용자 본인의 상품이어야 합니다."),

    //Product Admin
    INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST, "PA01", "잘못된 상태 변경 요청입니다."),
    PRODUCT_ALREADY_INSPECTED(HttpStatus.BAD_REQUEST, "PA02", "이미 검수가 완료된 상품입니다."),


    // Bid
    SELF_BIDDING_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "B01", "본인 상품을 입찰할 수 없습니다."),
    INVALID_BIDDING_PRICE(HttpStatus.BAD_REQUEST, "B02", "입찰 금액은 경매 시작 금액 이상이어야 합니다."),

    // Like

    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
