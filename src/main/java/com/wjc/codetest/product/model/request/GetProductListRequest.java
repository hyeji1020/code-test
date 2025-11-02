package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

/*
 * 해당 코멘트는 CreateProductRequest.java의 리뷰와 동일합니다.
 * 공통문제 및 원인:
 * 1. @Setter 사용 - RequestDTO 값은 변경 될 일이 없기 때문에 불필요.
 * 2. 기본 생성자 누락 - 클라이언트가 보낸 객체를 Java 객체로 변환하지 못하는 에러 발생.
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
public class GetProductListRequest {
    private String category;
    private int page;
    private int size;

    /*
     * 문제 및 원인:
     * 1. 조회 조건 DTO에 페이징 필드(page, size)를 함께 두면, 각 도메인별로 페이징 필드가 반복 정의될 수 있음.
     * 2. 현재 프로젝트가 Spring Data JPA 기반이므로, 페이징은 컨트롤러 단에서 Spring Data가 제공하는 Pageable/@PageableDefault로
     *    일관되게 처리하고, 필터링 조건만 별도 DTO로 분리하는 편이 책임이 명확함.
     *
     * 개선안:
     * 1. 해당 클래스에서 페이징 관련 필드(page, size) 삭제
     * 2. Controller에서 Pageable을 받아 페이징 처리. (ProductController.java에 관련 리뷰 남겼습니다.)
     *
     * 참고링크: https://claude.ai/share/7470fe9c-eec2-4470-8d4f-72fe6ced86ee
     */
}