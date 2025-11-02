package com.wjc.codetest.product.model.response;

import lombok.Getter;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author : 변영우 byw1666@wjcompass.com
 * @since : 2025-10-27
 */
/*
 * 공통문제 및 원인:
 * 1. @Setter 사용 - 응답 DTO는 불변이어야 하지만 setter로 수정 가능.
 *
 * 개선안:
 * 1. @Setter 삭제
 * 2. Record를 사용하면 자동으로 처리하여 더욱 간결하게 수정.
 */
@Getter
public class ProductListResponse {
    /*
    * 문제 및 원인: Entity 반환
    * 1. DB 유출 가능성.
    * 2. 연관 관계 포함 시 N+1 문제 발생 가능성.
    * 개선안: ProductResponse를 생성하여 필요한 필드만 반환.
    * */
    private final List<ProductResponse> products;
    private final int totalPages;
    private final long totalElements;
    private final int page;

    public ProductListResponse(List<ProductResponse> products, int totalPages, long totalElements, int number) {
        this.products = products;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = number;
    }
}
