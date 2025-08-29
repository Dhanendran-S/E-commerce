package com.example.E_commerce.Product.controller;


import com.example.E_commerce.Persistance.utils.ProductMapper;
import com.example.E_commerce.Product.assembler.ProductAssembler;
import com.example.E_commerce.Product.dto.ProductRequestDTO;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import com.example.E_commerce.Product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static com.example.E_commerce.Constants.CommonConstants.P_DELETED;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductAssembler productAssembler;
    private final ProductMapper productMapper;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    public ProductController(ProductService productService, ProductAssembler productAssembler, ProductMapper productMapper, PagedResourcesAssembler pagedResourcesAssembler) {
        this.productAssembler = productAssembler;
        this.productMapper = productMapper;
        this.productService = productService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/all")
    public PagedModel<EntityModel<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Page<ProductResponseDTO> products = productService.getAllProducts(filter, page, size, sortBy, sortDir);
        return pagedResourcesAssembler.toModel(products, productAssembler);
    }

    @GetMapping("/product/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id,
                                             @RequestParam(name = "lang", required = false, defaultValue = "en") String lang){
        Locale locale = Locale.forLanguageTag(lang);
        return productService.getProductById(id,locale);
    }

    @PostMapping("/add")
    public EntityModel<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO product,
                                                      @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        ProductResponseDTO savedProduct = productService.addProduct(product, locale);
        return productAssembler.toModel(savedProduct);
    }

    @PutMapping("/update/{id}")
    public EntityModel<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto,
                                                         @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        ProductResponseDTO updatedProduct = productService.updateProduct(id, dto, locale);
        return productAssembler.toModel(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        productService.deleteProduct(id, locale);
        return ResponseEntity.ok(P_DELETED);
    }

    /*@GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }*/
}
