package com.wjc.codetest.core.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BaseException{

    public ProductNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCode.PRODUCT_NOT_FOUND);
    }

}
