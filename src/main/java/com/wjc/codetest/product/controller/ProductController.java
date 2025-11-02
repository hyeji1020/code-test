package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
/*
* 문제: 클래스 전역 RequestMapping 미지정 및 단수형(product) 사용, Restful API 네이밍 규칙은 복수형 권장.
* 원인: 각 메서드마다 '/product'를 개별 지정하여 중복 발생, 복수형 규칙 사용.
* 개선안: @RequestMapping -> @RequestMapping("/products")
*
* ps. 프론트엔드와의 협업과 버전관리를 고려할 때,
* 페이지 라우트와 데이터 요청을 정확히 구분하기 위해 ("/api/v1/products")로 명시하는 방법도 있습니다.
*/
@RequestMapping
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /* 공통 문제:
     * 1. URL 경로에 HTTP 메서드(get, create, update, delete)를 포함하여 RESTful API 네이밍 규칙 위반.
     * 2. 동작에 맞지 않는 HTTP 메서드 사용.
     * 3. 응답 DTO가 아닌 Entity 반환하여 노출되는 문제 발생.
     * 4. 요청 DTO에 @Valid 누락.
     *
     * 개선안:
     * 1. 리소스 중심 설계로 변경, 명사로 표현.
     * 2. 동작에 맞게 HTTP 메서드 변경.
     * 3. Entity는 DB와 직접적으로 연결되는 객체라 함부로 노출되면 안 되기 때문에,
     * DB와 직접 연결되는 Repository의 전 단계 Service에서 변환해주는 것이 적합.
     * 4. DTO에서 유효성 검증을 한다는 전제하에, 요청 DTO에는 @Valid를 추가해야 함.
     *
     * ps. 원인과 수정 전,후 코드는 각 메서드별로 기재하였습니다. 코드가 길어지는 경우 수정후 코드만 기재하였습니다.
     */

    /*
     * 원인: URL에 HTTP 메서드(get), 조건(by) 포함하여 네이밍 규칙 위반
     * 개선안: @GetMapping(value = "/get/product/by/{productId}") -> @GetMapping(value = "/{productId}")
     */
    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    /*
     * 개선안: @PostMapping(value = "/create/product") -> @PostMapping
     */
    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(product);
    }

    /*
     * 문제: 
     * 1. Restful API 네이밍 원칙 위반 
     * 2. 불필요한 true를 반환.
     *
     * 원인: 
     * 1. 삭제 메서드이기 때문에 @DeleteMapping 사용
     * 2. HTTP 상태코드로 성공 여부를 알 수 있기 때문에 불필요한 ture를 반환하지 않아도 됨.
     *
     * 개선안:
     * 1. @PostMapping(value = "/delete/product/{productId}") -> @DeleteMapping(value = "/{productId}")
     * 2. 응답 DTO를 사용해 팀 컨벤션에 맞게 삭제 결과 정보를 포함하거나, HTTP 204(No Content) 상태코드로 반환.
     */
    @PostMapping(value = "/delete/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    /*
     * 원인:
     * 1. update 수행하는 메서드에 @PostMapping 사용.
     * 2. 수정하려는 객체를 식별하는 ID를 RequestBody에 포함.
     *
     * 개선안:
     * 1. 현재 UpdateProductRequest 내부의 생성자를 통해 일부 필드 수정용으로 추측되어, @PatchMapping 사용.
     * @PostMapping(value = "/update/product") -> @PatchMapping("/{productId}")
     * 2. 기능상 문제는 없지만, URI에 @PathVariable로 ID 값을 받아 어떤 객체를 수정하여 직관적으로 명시.
     * updateProduct(@Valid @RequestBody UpdateProductRequest dto, @PathVariable(name = "productId") Long productId)
     */
    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }

    /*
     * 문제 및 원인:
     * 1. 메서드명을 통해 조회 메서드로 추측되지만 @PostMapping 사용.
     * 2. 에러 발생 가능성 - RequestDto에 유효성 검증을 하지 않아 에러 발생 가능성.
     * 3. 편리성 저하 - 페이징 관련 데이터를 자동 바인딩 되는 Pageable 활용하지 않음.
     *
     * 개선안:
     * 1. 조회 HTTP 메서드 @GetMapping 사용과, 추후 필터링 데이터가 추가될 경우를 고려해서 @RequestParam 대신 @ModelAttribute 사용.
     * 2, 3. pageable 객체 생성 없이 기본값을 자동으로 적용시키는 @PageableDefault 사용하여 편리성과 안정성 높임.
     */
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    /*
    * 개선안: @GetMapping(value = "/product/category/list") -> @GetMapping
    */
    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}