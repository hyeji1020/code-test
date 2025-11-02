package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.model.response.ProductResponse;
import com.wjc.codetest.product.model.validator.ProductValidator;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 * 문제: @Slf4j 선언으로 가독성 저하.
 * 원인: @Slf4j 어노테이션을 선언했지만 코드에서 로깅을 하고 있지 않음.
 * 개선안:
 * 1. 로그가 필요하다면 레벨(INFO, WARN, ERROR 등)에 맞게 로그 작성.
 * 2. 로그를 사용하지 않는다면 @Slf4j 어노테이션을 제거.
 * TODO: 로깅 설정
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Transactional
    public ProductResponse create(CreateProductRequest dto) {

        Product product = Product.create(dto.category(), dto.name());
        Product createdProduct = productRepository.save(product);

        return ProductResponse.of(createdProduct);
    }

    /*
     * 문제: 수동 Optional 처리와 불명확한 예외 처리로 인한 가독성 및 유지보수성 저하.
     * 원인:
     * 1. Optional.isPresent() 수동으로 처리하여 코드량 증가.
     * 2. 범용적인 RuntimeException 사용으로 예외 원인 파악 어려움.
     * 개선안:
     * 1. Optional.orElseThrow()를 활용하여 간결하게 표현.
     * 2. ProductNotFoundException과 같은 커스텀 예외처리를 통해 원인을 명확히 전달.
     *
     * 트레이드 오프:
     * - 도메인별 커스텀 예외 처리로 클래스 파일 증가.
     * - 명확한 예외 처리 가능
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return ProductResponse.of(productValidator.validate(productId));
    }

    /*
     * 문제: setter를 사용한 엔티티 값 변경은 어디서나 변경 가능하여 문제 발생
     * 원인: product.setCategory(dto.getCategory()), product.setName(dto.getName())
     * 개선안: product Entity에 수정이 필요한 필드에 대한 update 메서드 생성.
     */
    @Transactional
    public ProductResponse updateProductById(Long productId, UpdateProductRequest dto) {

        Product product = productValidator.validate(productId);

        product.update(dto.category(), dto.name());

        return ProductResponse.of(product);
    }

    @Transactional
    public void deleteProductById(Long productId) {
        productRepository.delete(productValidator.validate(productId));
    }

    /*
     * 문제:
     * 1. Service 계층에서 요청 값에 대한 로직을 PageRequest.of()로 변환하여 계층 역할 불분명.
     * 2. RequestDto 유효성 검증이 안되어있기 때문에 값 보장 불가.
     * 원인:
     * 1. PageRequest.of(..)를 Service에서 생성
     * 2. GetProductListRequest에 유효성 검증 누락
     * 개선안:
     * 1. Service는 비즈니스 로직에 집중해야 하기 때문에 요청 값에 대한 로직은 Controller에서 수행.
     * 2. DTO에서 유효성 검증 어노테이션 추가하여 사전에 차단.
     */
    @Transactional(readOnly = true)
    public ProductListResponse getListByCategory(String category, Pageable pageable) {

        Page<Product> productPage = productRepository.findAllByCategory(category, pageable);

        List<ProductResponse> productResponseList = productPage.getContent()
                .stream()
                .map(ProductResponse::of)
                .toList();

        return new ProductListResponse(
                productResponseList,
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getNumber()
        );

    }

    @Transactional(readOnly = true)
    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}