package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
/*
 * 문제: 클래스 전체에 @Setter 선언시 외부에서 필드 값이 변경될 위험.
 * 원인: 외부 객체가 set 메서드를 통해 엔티티의 상태 변경 가능.
 * 개선안: @Setter 사용을 지양하거나 수정이 필요한 필드에만 사용.
 */
@Setter
public class Product {

    /*
     * 문제: 제약조건을 설정하지 않음.
     * 원인: DB에서는 컬럼에 제약 조건을 걸어두었지만, JPA 엔티티에서는 명시하지 않아 일관성이 깨짐.
     * JPA로 스키마를 생성하여 코드 기반으로 테이블 관리한다면 의도와 다르게 테이블이 생성될 수 있음.
     * DB 생성시에는 제약조건이 있지만, 해당 코드에서 없다면 불일치 문제 예상.
     * 개선안: 실제 DB 스키마에 맞게 엔티티에도 컬럼 제약을 명시.
     * @Id
     * @Column(name = "product_id, nullable = false")
     * @GeneratedValue(strategy = GenerationType.AUTO)
     * private Long id;
     *
     * @Column(name = "category", length = 255")
     * private String category;
     *
     * @Column(name = "name", length = 255)
     * private String name;
     *
     * ps. 실제 db 스키마 기반으로 제약조건 참고하였습니다.
     */

    @Id
    @Column(name = "product_id")
    /*
     * 문제: 현재 환경이 H2로 확정되어 있는데, AUTO 전략 사용.
     * 원인: AUTO 전략은 DB 벤더에 맞게 동적으로 바뀌기 때문에 DB 환경 예측 범위가 넓음.
     *
     * 개선안:
     * 동작상 문제는 없지만 현재는 H2 데이터베이스 환경이 정해져 있어서, 환경에 대한 명시성을 높이기 위해 SEQUENCE 전략 사용.
     * 하지만 추후에 DB 변경 가능성이 있다면 그대로 AUTO 전략을 사용하는게 바람직함.
     * @GeneratedValue(strategy = GenerationType.AUTO) -> @GeneratedValue(strategy = GenerationType.SEQUENCE)
     */
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    /*
     * 문제: 프로젝트 환경에 Lombok이 명시되어 있으니, 일관성을 위해 Lombok 사용 권장.
     * 원인: Lombok의 자동 생성 기능을 사용하지 않음.
     * 개선안: @NoArgsConstructor(access = AccessLevel.PROTECTED)로 대체 가능.
     */
    protected Product() {
    }

    /*
     * 문제:
     * 1. 위와 같은 필드 제약조건(ex. "nullable = false")과 상관없이 객체 생성 가능.
     * 2. 생성자 파라미터 변경시, 해당 생성자 사용 코드 찾아서 수정해야함.
     * 3. 속성 컨텍스트에 존재하는 동일 ID 엔티티를 또 생성하면, 영속성 컨텍스트 충돌 문제 발생.
     *
     * 원인:
     * 1. public은 외부에서 모두 접근 가능하여 자유롭게 객체 생성 가능.
     * 2. 직접 new Product()로 객체 생성하기 때문에, 기존 생성자 파라미터 구조대로 작성해야함.
     * 3. new Product()로 생성한 객체는 영속성 컨텍스트 관리 대상이 아니기 때문에,
     * 동일한 ID를 가진 객체가 존재한다면 충돌 문제 발생.
     *
     * 개선안:
     * 1. 생성자를 protected로 제한하여 외부 직접 생성 방지.
     * 2. public static 으로 객체 생성 없이 메서드 호출 가능하게 하고, 내부에 필드 제약조건 추가.
     *
     * ps. 만약 테스트 전용 객체 생성이 필요하다면, Fixture를 별도로 두어 관리하는 방법도 있을 것 같습니다.
     */
    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    /*
     * 문제: Lombok의 @Getter와 수동 getter의 의미가 중복되어 가독성 저하.
     * 원인: 동일한 이름의 getter 메서드를 직접 작성하면 Lombok은 해당 메서드를 생성하지 않음.
     * 개선안: 수동으로 getter를 생성한다면 필드가 많아질수록 코드량이 증가하므로,
     * Lombok의 @Getter를 사용하여 효율성과 가독성을 높임.
     */
    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
