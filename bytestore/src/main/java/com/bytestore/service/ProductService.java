package com.bytestore.service;

import com.bytestore.dto.ProductRequestDTO;
import com.bytestore.dto.ProductResponseDTO;
import com.bytestore.entity.Product;
import com.bytestore.exception.ProductNameAlreadyExistsException;
import com.bytestore.exception.ProductNotFoundException;
import com.bytestore.exception.ValidationException;
import com.bytestore.mapper.ProductMapper;
import com.bytestore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        validateProductData(dto);

        productRepository.findByNameIgnoreCase(dto.name()).ifPresent(p -> {
            throw new ProductNameAlreadyExistsException(dto.name());
        });

        Product product = Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .category(dto.category())
                .stockQuantity(dto.stockQuantity())
                .build();

        Product saved = productRepository.save(product);
        return productMapper.toResponseDTO(saved);
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO dto) {
        validateProductData(dto);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.findByNameIgnoreCase(dto.name()).ifPresent(other -> {
            if (!other.getId().equals(product.getId())) {
                throw new ProductNameAlreadyExistsException(dto.name(), other.getId());
            }
        });

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCategory(dto.category());
        product.setStockQuantity(dto.stockQuantity());

        Product updated = productRepository.save(product);
        return productMapper.toResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponseDTO(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(existing);
    }

    private void validateProductData(ProductRequestDTO dto) {
        if (dto.price() != null && dto.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("price", "O preço não pode ser negativo.");
        }

        if (dto.stockQuantity() != null && dto.stockQuantity() < 0) {
            throw new ValidationException("stockQuantity", "A quantidade em estoque não pode ser negativa.");
        }
    }
}
