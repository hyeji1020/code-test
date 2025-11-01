package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * 해당 코멘트는 CreateProductRequest.java의 리뷰와 동일합니다.
 * 공통문제 및 원인:
 * 1. @Setter 사용 - RequestDTO 값은 변경 될 일이 없기 때문에 불필요.
 * 2. 기본 생성자 누락 - 인하여 클라이언트가 보낸 객체를 JSON으로 변환하지 못하는 에러 발생.
 * 3. 유효성 검증 누락 - DB 스키마에 맞지 않는 값이나 null 값 전달하여 에러 발생.
 *
 * 개선안:
 * 1. @Setter 삭제
 * 2. @NoArgsConstructor 추가
 * 3. 각 필드에 맞게 @NotBlank(), @Size() 추가
 * 4. Record를 사용하여 1, 2번 개선사항을 자동으로 처리하여 더욱 간결하게 수정.
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    private Long id; // Controller에서 @PathVariable로 받아 Restful하게 변경.
    private String category;
    private String name;

    public UpdateProductRequest(Long id) {
        this.id = id;
    }

    public UpdateProductRequest(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public UpdateProductRequest(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}

