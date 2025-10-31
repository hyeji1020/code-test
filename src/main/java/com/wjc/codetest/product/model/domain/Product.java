package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
/*
 * 문제: 클래스 전체에 @Setter 선언시 외부에서 필드 값이 변경될 위험이 있음.
 * 원인: 외부 객체가 set 메서드를 통해 엔티티의 상태를 변경할 수 있고, 영속성 컨텍스트 불일치가 발생할 수 있음.
 * 개선안: @Setter 사용을 지양하거나 수정이 필요한 필드에만 사용해야함.
 *  생성자나 명확한 메서드를 통해 상태를 변경하도록 제한하는 것이 바람직함.
 */
@Setter
/*
 * 문제: Product 클래스명이 도메인 객체인지 엔티티인지 모호함.
 * 원인: 해당 클래스는 entity로 사용이 되고 있는데, Entity라고 명시해주지 않아 일반 도메인 객체로 보임.
 * 개선안: Product -> ProductEntity
 */
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    /*
     * 문제: Lombok의 자동 생성 기능을 사용하지 않음.
     * 개선안: @NoArgsConstructor(access = AccessLevel.PROTECTED)로 대체 가능.
     */
    protected Product() {
    }

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
