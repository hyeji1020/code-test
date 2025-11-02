package com.wjc.codetest.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    PRODUCT_NOT_FOUND("해당 상품을 찾을 수 없습니다."),
    BAD_REQUEST("잘못된 요청입니다.")
    ;

    private final String message;

}
