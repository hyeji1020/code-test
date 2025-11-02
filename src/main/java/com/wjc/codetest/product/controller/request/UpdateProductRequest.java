package com.wjc.codetest.product.controller.request;

import org.hibernate.validator.constraints.Length;

public record UpdateProductRequest(

        @Length(min = 1, max = 255, message = "카테고리는 255자 이내로 작성해주세요.")
        String category,

        @Length(min = 1, max = 255, message = "상품명은 255자 이내로 작성해주세요.")
        String name
){}


