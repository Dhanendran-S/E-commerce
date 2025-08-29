package com.example.E_commerce.Product.service;

import com.example.E_commerce.Exception.ProductNotFoundException;
import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Persistance.repository.ProductRepository;
import com.example.E_commerce.Persistance.utils.ProductMapper;
import com.example.E_commerce.Product.assembler.ProductAssembler;
import com.example.E_commerce.Product.dto.ProductRequestDTO;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;
import java.util.Optional;

import static com.example.E_commerce.Constants.CommonConstants.P_NOTFOUND;
import static com.example.E_commerce.Constants.CommonConstants.P_SAME;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductAssembler productAssembler;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public ProductService(ProductRepository productRepository, ProductAssembler productAssembler, ProductMapper productMapper, MessageSource messageSource, LocaleResolver localeResolver) {
        this.productRepository = productRepository;
        this.productAssembler = productAssembler;
        this.productMapper = productMapper;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    public Page<ProductResponseDTO> getAllProducts(String filter, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> spec = (root, query, cb) -> {
            if (filter != null && !filter.isEmpty()) {
                try {
                    Double price = Double.parseDouble(filter);
                    return cb.equal(root.get("price"), price);
                } catch (NumberFormatException e) {
                    return cb.conjunction();
                }
            }
            return cb.conjunction();
        };

        return productRepository.findAll(spec, pageable)
                .map(ProductMapper::toResponseDTO);
    }

    public ProductResponseDTO getProductById(Long id, Locale locale) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(messageSource.getMessage("product_not_found", null, locale)));
        return productAssembler.toResponseDTO(product);
    }

    public ProductResponseDTO addProduct(ProductRequestDTO dto, Locale locale) {
        if (productRepository.existsByName(dto.getName())) {
            throw new ProductNotFoundException(messageSource.getMessage("Product_name_same", null, locale));
        }
        Product product = productMapper.toEntity(dto);
        Product added = productRepository.save(product);
        return productAssembler.toResponseDTO(added);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto, Locale locale) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product =  productOptional.get();
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice());
            product.setStockQty(dto.getQuantity());
            return productAssembler.toResponseDTO(productRepository.save(product));
        }
        else {
            throw new ProductNotFoundException(messageSource.getMessage("product_not_found", null, locale));
        }
    }

    public void deleteProduct(Long id, Locale locale) {
        if(!productRepository.existsById(id)) {
            throw new ProductNotFoundException(messageSource.getMessage("product_not_found", null, locale));
        }
        productRepository.deleteById(id);
    }
}
