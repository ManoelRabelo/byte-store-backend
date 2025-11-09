package com.bytestore.entity;

import com.bytestore.exception.InsufficientStockException;
import com.bytestore.exception.ValidationException;
import com.bytestore.util.TestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void should_returnTrue_when_hasEnoughStock() {
        Product product = TestDataBuilder.product()
                .stockQuantity(10)
                .build();

        boolean hasStock = product.hasStock(5);

        assertThat(hasStock).isTrue();
    }

    @Test
    void should_returnTrue_when_hasExactStock() {
        Product product = TestDataBuilder.product()
                .stockQuantity(10)
                .build();

        boolean hasStock = product.hasStock(10);

        assertThat(hasStock).isTrue();
    }

    @Test
    void should_returnFalse_when_hasInsufficientStock() {
        Product product = TestDataBuilder.product()
                .stockQuantity(5)
                .build();

        boolean hasStock = product.hasStock(10);

        assertThat(hasStock).isFalse();
    }

    @Test
    void should_decreaseStock_when_hasEnoughStock() {
        Product product = TestDataBuilder.product()
                .stockQuantity(10)
                .build();

        product.decreaseStock(3);

        assertThat(product.getStockQuantity()).isEqualTo(7);
    }

    @Test
    void should_decreaseStock_when_hasExactStock() {
        Product product = TestDataBuilder.product()
                .stockQuantity(10)
                .build();

        product.decreaseStock(10);

        assertThat(product.getStockQuantity()).isEqualTo(0);
    }

    @Test
    void should_throwInsufficientStockException_when_hasInsufficientStock() {
        Product product = TestDataBuilder.product()
                .name("Produto Teste")
                .stockQuantity(5)
                .build();

        assertThatThrownBy(() -> product.decreaseStock(10))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Estoque insuficiente")
                .hasMessageContaining("Produto Teste")
                .hasMessageContaining("5")
                .hasMessageContaining("10");
    }

    @Test
    void should_throwValidationException_when_quantityIsNull() {
        Product product = TestDataBuilder.product()
                .stockQuantity(10)
                .build();

        assertThatThrownBy(() -> product.decreaseStock(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("maior que zero");
    }

    @Test
    void should_throwValidationException_when_quantityIsZero() {
        Product product = TestDataBuilder.product()
                .stockQuantity(10)
                .build();

        assertThatThrownBy(() -> product.decreaseStock(0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("maior que zero");
    }

    @Test
    void should_throwValidationException_when_quantityIsNegative() {
        Product product = TestDataBuilder.product()
                .stockQuantity(10)
                .build();

        assertThatThrownBy(() -> product.decreaseStock(-1))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("maior que zero");
    }
}
