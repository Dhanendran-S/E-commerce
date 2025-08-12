package com.example.E_commerce.Product.controller;


import com.example.E_commerce.Persistance.utils.ProductMapper;
import com.example.E_commerce.Product.assembler.ProductAssembler;
import com.example.E_commerce.Product.dto.ProductRequestDTO;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import com.example.E_commerce.Product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PostMapping("/add")
    public EntityModel<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO product) {
        ProductResponseDTO savedProduct = productService.addProduct(product);
        return productAssembler.toModel(savedProduct);
    }

    @PutMapping("/{id}")
    public EntityModel<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, dto);
        return productAssembler.toModel(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(P_DELETED);
    }
}
