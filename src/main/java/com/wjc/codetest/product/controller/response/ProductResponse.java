package com.wjc.codetest.product.controller.response;

import com.wjc.codetest.product.model.domain.Product;

public record ProductResponse(String category, String name){

    // Entity -> DTO 변환 메서드
    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getCategory(),
                product.getName()
        );
    }

}
