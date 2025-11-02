package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;
import lombok.Setter;

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
 * 2. Record를 사용하여 1의 개선사항을 자동으로 처리하여 더욱 간결하게 수정.
 */
@Getter
@Setter
public class ProductListResponse {
    /*
    * 문제 및 원인: Entity 반환
    * 1. DB 유출 가능성.
    * 2. 연관 관계 포함 시 N+1 문제 발생 가능성.
    * 개선안: ProductResponse를 생성하여 직접적인 Entity 사용 X.
    * */
    private List<Product> products;
    private int totalPages;
    private long totalElements;
    private int page;

    public ProductListResponse(List<Product> content, int totalPages, long totalElements, int number) {
        this.products = content; // 매개변수명과 필드명 일치하지 않아 혼란 -> 동일하게 수정
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = number;
    }
}
