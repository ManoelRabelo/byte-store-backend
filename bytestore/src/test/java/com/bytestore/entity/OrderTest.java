package com.bytestore.entity;

import com.bytestore.util.TestDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void should_calculateTotalAmount_when_hasItems() {
        Order order = TestDataBuilder.order().build();
        
        Product product1 = TestDataBuilder.product()
                .price(BigDecimal.valueOf(100.00))
                .build();
        Product product2 = TestDataBuilder.product()
                .price(BigDecimal.valueOf(50.00))
                .build();
        
        OrderItem item1 = TestDataBuilder.orderItem()
                .product(product1)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(200.00))
                .build();
        
        OrderItem item2 = TestDataBuilder.orderItem()
                .product(product2)
                .quantity(3)
                .unitPrice(BigDecimal.valueOf(50.00))
                .subtotal(BigDecimal.valueOf(150.00))
                .build();
        
        order.addOrderItem(item1);
        order.addOrderItem(item2);
        
        order.calculateTotalAmount();
        
        assertThat(order.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(350.00));
    }

    @Test
    void should_calculateTotalAmount_when_hasSingleItem() {
        Order order = TestDataBuilder.order().build();
        
        Product product = TestDataBuilder.product()
                .price(BigDecimal.valueOf(100.00))
                .build();
        
        OrderItem item = TestDataBuilder.orderItem()
                .product(product)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(200.00))
                .build();
        
        order.addOrderItem(item);
        
        order.calculateTotalAmount();
        
        assertThat(order.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
    }

    @Test
    void should_returnZero_when_hasNoItems() {
        Order order = TestDataBuilder.order().build();
        
        order.calculateTotalAmount();
        
        assertThat(order.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void should_returnZero_when_itemsListIsNull() {
        Order order = TestDataBuilder.order()
                .orderItems(null)
                .build();
        
        order.calculateTotalAmount();
        
        assertThat(order.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void should_addOrderItem_and_setOrderReference() {
        Order order = TestDataBuilder.order().build();
        
        Product product = TestDataBuilder.product().build();
        
        OrderItem item = TestDataBuilder.orderItem()
                .product(product)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(200.00))
                .build();
        
        order.addOrderItem(item);
        
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(order.getOrderItems()).contains(item);
        assertThat(item.getOrder()).isEqualTo(order);
    }

    @Test
    void should_addMultipleItems() {
        Order order = TestDataBuilder.order().build();
        
        Product product1 = TestDataBuilder.product().build();
        Product product2 = TestDataBuilder.product().build();
        
        OrderItem item1 = TestDataBuilder.orderItem()
                .product(product1)
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(100.00))
                .build();
        
        OrderItem item2 = TestDataBuilder.orderItem()
                .product(product2)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(50.00))
                .subtotal(BigDecimal.valueOf(100.00))
                .build();
        
        order.addOrderItem(item1);
        order.addOrderItem(item2);
        
        assertThat(order.getOrderItems()).hasSize(2);
        assertThat(order.getOrderItems()).contains(item1, item2);
        assertThat(item1.getOrder()).isEqualTo(order);
        assertThat(item2.getOrder()).isEqualTo(order);
    }
}

