package com.example.E_commerce.Product.service;

import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Persistance.repository.ProductRepository;
import com.example.E_commerce.Persistance.utils.ProductMapper;
import com.example.E_commerce.Product.assembler.ProductAssembler;
import com.example.E_commerce.Product.dto.ProductRequestDTO;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductAssembler productAssembler;

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


    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponseDTO(product);

    }

    public ProductResponseDTO addProduct(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);
        Product added = productRepository.save(product);
        return productMapper.toResponseDTO(added);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product =  productOptional.get();
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice());
            product.setStockQty(dto.getQuantity());
            return productMapper.toResponseDTO(productRepository.save(product));
        }
        else {
            throw new RuntimeException("Product with ID " + id + " not found");
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
