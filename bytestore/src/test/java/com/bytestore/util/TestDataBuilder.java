package com.bytestore.util;

import com.bytestore.dto.*;
import com.bytestore.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Classe para criar objetos de teste.
public class TestDataBuilder {

    // ========== PRODUCT ==========

    public static Product.ProductBuilder product() {
        return Product.builder()
                .id(UUID.randomUUID())
                .name("Produto Teste")
                .description("Descrição do produto teste")
                .price(BigDecimal.valueOf(100.00))
                .category("Categoria Teste")
                .stockQuantity(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());
    }

    public static Product defaultProduct() {
        return product().build();
    }

    // ========== PRODUCT REQUEST DTO ==========

    public static ProductRequestDTO productRequestDTO() {
        return productRequestDTO("Produto Teste", BigDecimal.valueOf(100.00), 10);
    }

    public static ProductRequestDTO productRequestDTO(String name, BigDecimal price, Integer stockQuantity) {
        return new ProductRequestDTO(
                name,
                "Descrição do produto",
                price,
                "Categoria",
                stockQuantity
        );
    }

    // ========== PRODUCT RESPONSE DTO ==========

    public static ProductResponseDTO productResponseDTO() {
        return productResponseDTO(UUID.randomUUID(), "Produto Teste");
    }

    public static ProductResponseDTO productResponseDTO(UUID id, String name) {
        return new ProductResponseDTO(
                id,
                name,
                "Descrição do produto",
                BigDecimal.valueOf(100.00),
                "Categoria",
                10,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // ========== USER ==========

    public static User.UserBuilder user() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Usuário Teste")
                .email("usuario@teste.com")
                .password("senha123")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .orders(new ArrayList<>());
    }

    public static User defaultUser() {
        return user().build();
    }

    public static User adminUser() {
        return user()
                .email("admin@teste.com")
                .role(Role.ADMIN)
                .build();
    }

    // ========== ORDER ==========

    public static Order.OrderBuilder order() {
        return Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.PENDENTE)
                .totalAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .orderItems(new ArrayList<>());
    }

    public static Order defaultOrder() {
        return order().build();
    }

    public static Order orderWithUser(User user, OrderStatus status) {
        return order()
                .user(user)
                .status(status)
                .build();
    }

    // ========== ORDER ITEM ==========

    public static OrderItem.OrderItemBuilder orderItem() {
        return OrderItem.builder()
                .id(UUID.randomUUID())
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(200.00));
    }

    public static OrderItem orderItemWithProduct(Product product, Integer quantity) {
        OrderItem item = orderItem()
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .build();
        item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    // ========== ORDER REQUEST DTO ==========

    public static OrderRequestDTO orderRequestDTO() {
        UUID productId = UUID.randomUUID();
        return orderRequestDTO(productId, 2);
    }

    public static OrderRequestDTO orderRequestDTO(UUID productId, Integer quantity) {
        return new OrderRequestDTO(
                List.of(new OrderItemRequestDTO(productId, quantity))
        );
    }

    public static OrderRequestDTO orderRequestDTOWithItems(List<OrderItemRequestDTO> items) {
        return new OrderRequestDTO(items);
    }

    // ========== ORDER ITEM REQUEST DTO ==========

    public static OrderItemRequestDTO orderItemRequestDTO(UUID productId, Integer quantity) {
        return new OrderItemRequestDTO(productId, quantity);
    }

    // ========== ORDER RESPONSE DTO ==========

    public static OrderResponseDTO orderResponseDTO() {
        return orderResponseDTO(UUID.randomUUID(), UUID.randomUUID(), OrderStatus.PENDENTE);
    }

    public static OrderResponseDTO orderResponseDTO(UUID orderId, UUID userId, OrderStatus status) {
        return new OrderResponseDTO(
                orderId,
                userId,
                status,
                new ArrayList<>(),
                BigDecimal.valueOf(200.00),
                LocalDateTime.now(),
                null
        );
    }

    public static OrderResponseDTO orderResponseDTOWithItems(UUID orderId, UUID userId,
                                                             List<OrderItemResponseDTO> items,
                                                             BigDecimal totalAmount) {
        return new OrderResponseDTO(
                orderId,
                userId,
                OrderStatus.PENDENTE,
                items,
                totalAmount,
                LocalDateTime.now(),
                null
        );
    }

    // ========== ORDER SUMMARY DTO ==========

    public static OrderSummaryDTO orderSummaryDTO() {
        return new OrderSummaryDTO(
                UUID.randomUUID(),
                OrderStatus.PENDENTE,
                BigDecimal.valueOf(200.00),
                2,
                LocalDateTime.now(),
                null
        );
    }

    // ========== ORDER ITEM RESPONSE DTO ==========

    public static OrderItemResponseDTO orderItemResponseDTO() {
        return new OrderItemResponseDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Produto Teste",
                2,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(200.00)
        );
    }

    // ========== AUTH REQUEST DTO ==========

    public static AuthRequestDTO authRequestDTO() {
        return new AuthRequestDTO("usuario@teste.com", "senha123");
    }

    public static AuthRequestDTO authRequestDTO(String email, String password) {
        return new AuthRequestDTO(email, password);
    }

    // ========== REGISTER REQUEST DTO ==========

    public static RegisterRequestDTO registerRequestDTO() {
        return new RegisterRequestDTO(
                "Usuário Teste",
                "usuario@teste.com",
                "senha123",
                Role.USER
        );
    }

    public static RegisterRequestDTO registerRequestDTO(String name, String email, String password, Role role) {
        return new RegisterRequestDTO(name, email, password, role);
    }

    // ========== AUTH RESPONSE DTO ==========

    public static AuthResponseDTO authResponseDTO() {
        return new AuthResponseDTO(
                "jwt-token-test",
                "Bearer",
                java.time.Instant.now().plusSeconds(3600),
                UUID.randomUUID(),
                "Usuário Teste",
                "usuario@teste.com",
                Role.USER
        );
    }

    public static AuthResponseDTO authResponseDTO(String accessToken, UUID userId, String email, Role role) {
        return new AuthResponseDTO(
                accessToken,
                "Bearer",
                java.time.Instant.now().plusSeconds(3600),
                userId,
                "Usuário Teste",
                email,
                role
        );
    }

    // ========== MÉTODOS AUXILIARES ==========

    public static List<Product> productList(int size) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            products.add(product()
                    .name("Produto " + (i + 1))
                    .price(BigDecimal.valueOf(10.00 * (i + 1)))
                    .build());
        }
        return products;
    }

    public static List<Order> orderList(int size, User user) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            orders.add(order()
                    .user(user)
                    .status(OrderStatus.PENDENTE)
                    .totalAmount(BigDecimal.valueOf(100.00 * (i + 1)))
                    .build());
        }
        return orders;
    }
}

