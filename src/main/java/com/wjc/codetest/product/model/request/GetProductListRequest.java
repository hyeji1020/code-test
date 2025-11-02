package com.wjc.codetest.product.model.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record GetProductListRequest(

        String category,
        Integer page,  // 페이지 번호, 기본값 (0)
        Integer size   // 페이지 크기, 기본값 (10)

) {

    // 기본값을 설정하는 생성자
    public GetProductListRequest {
        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 10;
    }

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
