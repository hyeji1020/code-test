package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
    * 문제: Service 계층까지 category 값을 전달했지만, Repository 메서드에서 파라미터명을 name으로 잘못 선언하여 의도와 다르게 동작.
    * 원인: String category 대신 String name으로 전달.
    * 개선안: findAllByCategory(String category, Pageable pageable);
    * */
    Page<Product> findAllByCategory(String name, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}
