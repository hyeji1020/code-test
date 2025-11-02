package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    private Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public static Product create(String category, String name) {
        return new Product(category, name);
    }

    public void update(String category, String name) {
        if (category != null) this.category = category;
        if (name != null) this.name = name;
    }

}
