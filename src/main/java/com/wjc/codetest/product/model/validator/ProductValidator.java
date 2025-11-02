package com.wjc.codetest.product.model.validator;


import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

    public Product validate(Long postingId) {
        return productRepository.findById(postingId)
                .orElseThrow(() -> new RuntimeException("product not found"));
    }

}
