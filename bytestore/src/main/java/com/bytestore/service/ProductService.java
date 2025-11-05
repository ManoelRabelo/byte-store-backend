package com.bytestore.service;

import com.bytestore.dto.ProductRequestDTO;
import com.bytestore.dto.ProductResponseDTO;
import com.bytestore.entity.Product;
import com.bytestore.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        validateProductData(dto);

        productRepository.findByNameIgnoreCase(dto.name()).ifPresent(p -> {
            throw new IllegalArgumentException("Já existe um produto com este nome.");
        });

        Product product = Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .category(dto.category())
                .stockQuantity(dto.stockQuantity())
                .build();

        Product saved = productRepository.save(product);
        return mapToResponseDTO(saved);
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO dto) {
        validateProductData(dto);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        productRepository.findByNameIgnoreCase(dto.name()).ifPresent(other -> {
            if (!other.getId().equals(product.getId())) {
                throw new IllegalArgumentException("Já existe outro produto com este nome.");
            }
        });

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCategory(dto.category());
        product.setStockQuantity(dto.stockQuantity());

        Product updated = productRepository.save(product);
        return mapToResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        return mapToResponseDTO(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        productRepository.delete(existing);
    }

    private void validateProductData(ProductRequestDTO dto) {
        if (dto.price() != null && dto.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }

        if (dto.stockQuantity() != null && dto.stockQuantity() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

}
