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
     *
     * 개선안:
     * 1. 해당 클래스에서 페이징 관련 필드(page, size) 삭제하여 별도의 PageRequestDto 생성.
     * 2. Controller에서 Pageable을 받아 페이징 처리.
     *
     * 개인 의견:
     * 1. @PageableDefault vs DTO 내 페이징 데이터 포함
     * Pageable의 기본 page 값은 0이기 때문에 프론트엔드와 협업 시 혼동이 발생할 수 있다고 생각했습니다.
     * @PageableDefault는 기본값을 직관적으로 설정하고 별도의 변환 없이 사용할 수 있다는 장점이 있지만,
     * 저는 프론트에서 page=1을 전달하더라도 DTO 생성자에서 0으로 보정하여 사용하는 방식이 더 명확하다고 판단했습니다.
     * 따라서 페이징 정보도 DTO에서 함께 관리하는 방식을 선택했습니다.
     * 
     * 2. 필터링 데이터와 페이징 데이터를 같은 DTO에 두는 문제
     * 페이지 정책이 모든 기능에서 일관성 있게 정해져있고, 프로젝트 규모가 커진다면 페이징 DTO를 분리할 것 같습니다.
     * 하지만 현재 프로젝트 규모를 고려할 때는, 함께 두어도 무방하다고 판단했습니다.
     *
     * 참고링크: https://claude.ai/share/2ad1a0c1-e2f7-42f4-9e38-2ef8e711a6a6
     */
}