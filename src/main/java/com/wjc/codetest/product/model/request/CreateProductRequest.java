package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
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
public class CreateProductRequest {
    private String category;
    private String name;

    /*
     * 문제: 불필요한 생성자
     * 현재 DB 스키마에서는 category, name에 대해 null을 허용하지만,
     * 일반적으로 상품 생성 시 필수 항목들이기 때문에 null을 허용하지 않아야 함.
     *
     * 원인:
     * category 인자만으로 product 생성가능한 생성자
     *
     * 개선안:
     * 아래와 같은 의미 없는 생성자는 불필요.
     */
    public CreateProductRequest(String category) {
        this.category = category;
    }

    /*
     * 문제: 불필요한 생성자
     * Lombok에서 제공하는 @AllArgsConstructor를 사용하지 않아 일관성이 깨짐.
     *
     * 개선안:
     * @AllArgsConstructor 추가
     */
    public CreateProductRequest(String category, String name) {
        this.category = category;
        this.name = name;
    }
}

