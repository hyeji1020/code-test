package com.wjc.codetest.product.model.request;

import com.wjc.codetest.product.model.domain.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

/*
 * 공통문제 및 원인:
 * 1. @Setter 사용 - RequestDTO 값은 변경 될 일이 없기 때문에 불필요.
 * 2. 기본 생성자 누락 - 클라이언트가 보낸 객체를 JSON으로 변환하지 못하는 에러 발생.
 * 3. 유효성 검증 누락 - DB 스키마에 맞지 않는 값이나 null 값 전달하여 에러 발생.
 *
 * 개선안:
 * 1. @Setter 삭제
 * 2. @NoArgsConstructor 추가
 * 3. 각 필드에 맞게 @NotBlank(), @Size() 추가
 * 4. Record를 사용하여 1, 2번 개선사항을 자동으로 처리하여 더욱 간결하게 수정.
 */
public record CreateProductRequest(

        @NotBlank(message = "카테고리는 필수 입력값입니다.")
        @Length(min = 1, max = 255, message = "카테고리는 255자 이내로 작성해주세요.")
        String category,

        @NotBlank(message = "상품명은 필수 입력값입니다.")
        @Length(min = 1, max = 255, message = "상품명은 255자 이내로 작성해주세요.")
        String name
        ){

}


