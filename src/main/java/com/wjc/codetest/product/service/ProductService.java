package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * 문제: @Slf4j 선언으로 가독성 저하.
 * 원인: @Slf4j 어노테이션을 선언했지만 코드에서 로깅을 하고 있지 않음.
 * 개선안:
 * 1. 로그가 필요하다면 레벨(INFO, WARN, ERROR 등)에 맞게 로그 작성.
 * 2. 로그를 사용하지 않는다면 @Slf4j 어노테이션을 제거.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /*
     * 공통 문제: @Transactional 누락으로 인한 데이터 일관성 및 성능 저하.
     * 개선안:
     * 1. 조회 메서드에는 @Transactional(readOnly = true) 추가.
     * 2. 생성/수정/삭제 메서드에는 @Transactional 추가.
     */
            
    public Product create(CreateProductRequest dto) {
         Product product = new Product(dto.getCategory(), dto.getName());

        return productRepository.save(product);
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
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    /*
     * 문제: setter를 사용한 엔티티 값 변경은 어디서나 변경 가능하여 문제 발생
     * 원인: product.setCategory(dto.getCategory()), product.setName(dto.getName())
     * 개선안: product Entity에 수정이 필요한 필드에 대한 update 메서드 생성.
     */
    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }

    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
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
    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}