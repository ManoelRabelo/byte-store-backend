package com.bytestore.service;

import com.bytestore.dto.ProductRequestDTO;
import com.bytestore.dto.ProductResponseDTO;
import com.bytestore.entity.Product;
import com.bytestore.exception.ProductNameAlreadyExistsException;
import com.bytestore.exception.ProductNotFoundException;
import com.bytestore.exception.ValidationException;
import com.bytestore.mapper.ProductMapper;
import com.bytestore.repository.ProductRepository;
import com.bytestore.util.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    // ========== CREATE PRODUCT ==========

    @Test
    void should_createProduct_when_validData() {
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO();
        Product savedProduct = TestDataBuilder.product()
                .name(requestDTO.name())
                .price(requestDTO.price())
                .build();
        ProductResponseDTO responseDTO = TestDataBuilder.productResponseDTO();

        when(productRepository.findByNameIgnoreCase(requestDTO.name())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toResponseDTO(savedProduct)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.createProduct(requestDTO);

        assertThat(result).isNotNull();
        verify(productRepository).findByNameIgnoreCase(requestDTO.name());
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toResponseDTO(savedProduct);
    }

    @Test
    void should_throwProductNameAlreadyExistsException_when_nameAlreadyExists() {
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO();
        Product existingProduct = TestDataBuilder.product()
                .name(requestDTO.name())
                .build();

        when(productRepository.findByNameIgnoreCase(requestDTO.name())).thenReturn(Optional.of(existingProduct));

        assertThatThrownBy(() -> productService.createProduct(requestDTO))
                .isInstanceOf(ProductNameAlreadyExistsException.class)
                .hasMessageContaining("Já existe um produto com o nome")
                .hasMessageContaining(requestDTO.name());

        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toResponseDTO(any());
    }

    @Test
    void should_throwValidationException_when_priceIsNegative() {
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO(
                "Produto Teste",
                BigDecimal.valueOf(-100.00),
                10
        );

        assertThatThrownBy(() -> productService.createProduct(requestDTO))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("price")
                .hasMessageContaining("não pode ser negativo");

        verify(productRepository, never()).findByNameIgnoreCase(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void should_throwValidationException_when_stockQuantityIsNegative() {
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO(
                "Produto Teste",
                BigDecimal.valueOf(100.00),
                -10
        );

        assertThatThrownBy(() -> productService.createProduct(requestDTO))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("stockQuantity")
                .hasMessageContaining("não pode ser negativa");

        verify(productRepository, never()).findByNameIgnoreCase(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void should_createProduct_when_priceIsZero() {
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO(
                "Produto Teste",
                BigDecimal.ZERO,
                10
        );
        Product savedProduct = TestDataBuilder.product()
                .name(requestDTO.name())
                .price(BigDecimal.ZERO)
                .build();
        ProductResponseDTO responseDTO = TestDataBuilder.productResponseDTO();

        when(productRepository.findByNameIgnoreCase(requestDTO.name())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toResponseDTO(savedProduct)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.createProduct(requestDTO);

        assertThat(result).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    // ========== UPDATE PRODUCT ==========

    @Test
    void should_updateProduct_when_validData() {
        UUID productId = UUID.randomUUID();
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO();
        Product existingProduct = TestDataBuilder.product()
                .id(productId)
                .name("Produto Antigo")
                .build();
        Product updatedProduct = TestDataBuilder.product()
                .id(productId)
                .name(requestDTO.name())
                .build();
        ProductResponseDTO responseDTO = TestDataBuilder.productResponseDTO();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.findByNameIgnoreCase(requestDTO.name())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toResponseDTO(updatedProduct)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.updateProduct(productId, requestDTO);

        assertThat(result).isNotNull();
        verify(productRepository).findById(productId);
        verify(productRepository).findByNameIgnoreCase(requestDTO.name());
        verify(productRepository).save(existingProduct);
        verify(productMapper).toResponseDTO(updatedProduct);
    }

    @Test
    void should_throwProductNotFoundException_when_updatingNonExistentProduct() {
        UUID productId = UUID.randomUUID();
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(productId, requestDTO))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toResponseDTO(any());
    }

    @Test
    void should_throwProductNameAlreadyExistsException_when_nameExistsForOtherProduct() {
        UUID productId = UUID.randomUUID();
        UUID otherProductId = UUID.randomUUID();
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO();
        Product existingProduct = TestDataBuilder.product()
                .id(productId)
                .build();
        Product otherProduct = TestDataBuilder.product()
                .id(otherProductId)
                .name(requestDTO.name())
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.findByNameIgnoreCase(requestDTO.name())).thenReturn(Optional.of(otherProduct));

        assertThatThrownBy(() -> productService.updateProduct(productId, requestDTO))
                .isInstanceOf(ProductNameAlreadyExistsException.class)
                .hasMessageContaining("Já existe outro produto com o nome")
                .hasMessageContaining(requestDTO.name());

        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toResponseDTO(any());
    }

    @Test
    void should_updateProduct_when_nameUnchanged() {
        UUID productId = UUID.randomUUID();
        String productName = "Produto Teste";
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO(productName, BigDecimal.valueOf(100.00), 10);
        Product existingProduct = TestDataBuilder.product()
                .id(productId)
                .name(productName)
                .build();
        Product updatedProduct = TestDataBuilder.product()
                .id(productId)
                .name(productName)
                .build();
        ProductResponseDTO responseDTO = TestDataBuilder.productResponseDTO();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.findByNameIgnoreCase(productName)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toResponseDTO(updatedProduct)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.updateProduct(productId, requestDTO);

        assertThat(result).isNotNull();
        verify(productRepository).save(existingProduct);
    }

    @Test
    void should_throwValidationException_when_updatePriceIsNegative() {
        UUID productId = UUID.randomUUID();
        ProductRequestDTO requestDTO = TestDataBuilder.productRequestDTO(
                "Produto Teste",
                BigDecimal.valueOf(-100.00),
                10
        );

        assertThatThrownBy(() -> productService.updateProduct(productId, requestDTO))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("price")
                .hasMessageContaining("não pode ser negativo");

        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    // ========== GET PRODUCT BY ID ==========

    @Test
    void should_returnProduct_when_productExists() {
        UUID productId = UUID.randomUUID();
        Product product = TestDataBuilder.product()
                .id(productId)
                .build();
        ProductResponseDTO responseDTO = TestDataBuilder.productResponseDTO();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.getProductById(productId);

        assertThat(result).isNotNull();
        verify(productRepository).findById(productId);
        verify(productMapper).toResponseDTO(product);
    }

    @Test
    void should_throwProductNotFoundException_when_gettingNonExistentProduct() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productMapper, never()).toResponseDTO(any());
    }

    // ========== GET ALL PRODUCTS ==========

    @Test
    void should_returnAllProducts_when_productsExist() {
        Product product1 = TestDataBuilder.product().build();
        Product product2 = TestDataBuilder.product().build();
        List<Product> products = List.of(product1, product2);
        ProductResponseDTO responseDTO1 = TestDataBuilder.productResponseDTO();
        ProductResponseDTO responseDTO2 = TestDataBuilder.productResponseDTO();

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toResponseDTO(product1)).thenReturn(responseDTO1);
        when(productMapper.toResponseDTO(product2)).thenReturn(responseDTO2);

        List<ProductResponseDTO> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        verify(productRepository).findAll();
        verify(productMapper, times(2)).toResponseDTO(any(Product.class));
    }

    @Test
    void should_returnEmptyList_when_noProductsExist() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductResponseDTO> result = productService.getAllProducts();

        assertThat(result).isEmpty();
        verify(productRepository).findAll();
        verify(productMapper, never()).toResponseDTO(any());
    }

    // ========== DELETE PRODUCT ==========

    @Test
    void should_deleteProduct_when_productExists() {
        UUID productId = UUID.randomUUID();
        Product product = TestDataBuilder.product()
                .id(productId)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
    }

    @Test
    void should_throwProductNotFoundException_when_deletingNonExistentProduct() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository, never()).delete(any());
    }
}

