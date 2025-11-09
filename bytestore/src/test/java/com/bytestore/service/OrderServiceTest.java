package com.bytestore.service;

import com.bytestore.dto.*;
import com.bytestore.entity.*;
import com.bytestore.exception.*;
import com.bytestore.mapper.OrderMapper;
import com.bytestore.repository.OrderRepository;
import com.bytestore.repository.ProductRepository;
import com.bytestore.repository.UserRepository;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    // ========== CREATE ORDER ==========

    @Test
    void should_createOrder_when_validItemsAndSufficientStock() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Product product = TestDataBuilder.product()
                .id(productId)
                .stockQuantity(10)
                .price(BigDecimal.valueOf(100.00))
                .build();

        OrderRequestDTO requestDTO = TestDataBuilder.orderRequestDTO(productId, 2);
        Order savedOrder = TestDataBuilder.order()
                .user(user)
                .status(OrderStatus.PENDENTE)
                .build();
        OrderResponseDTO responseDTO = TestDataBuilder.orderResponseDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponseDTO(savedOrder)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.createOrder(requestDTO, userId);

        assertThat(result).isNotNull();
        verify(userRepository).findById(userId);
        verify(productRepository).findById(productId);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toResponseDTO(savedOrder);
    }

    @Test
    void should_cancelOrder_when_insufficientStock() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Product product = TestDataBuilder.product()
                .id(productId)
                .name("Produto Teste")
                .stockQuantity(5)
                .price(BigDecimal.valueOf(100.00))
                .build();

        OrderRequestDTO requestDTO = TestDataBuilder.orderRequestDTO(productId, 10);
        Order cancelledOrder = TestDataBuilder.order()
                .user(user)
                .status(OrderStatus.CANCELADO)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(cancelledOrder);

        assertThatThrownBy(() -> orderService.createOrder(requestDTO, userId))
                .isInstanceOf(OrderCancelledDueToStockException.class)
                .hasMessageContaining("Pedido cancelado")
                .hasMessageContaining("Produto Teste");

        verify(orderRepository).save(argThat(order -> order.getStatus() == OrderStatus.CANCELADO));
    }

    @Test
    void should_throwValidationException_when_itemsIsEmpty() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        OrderRequestDTO requestDTO = new OrderRequestDTO(List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> orderService.createOrder(requestDTO, userId))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("items")
                .hasMessageContaining("pelo menos um item");

        verify(productRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_throwValidationException_when_itemsIsNull() {
        UUID userId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        OrderRequestDTO requestDTO = new OrderRequestDTO(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> orderService.createOrder(requestDTO, userId))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("items")
                .hasMessageContaining("pelo menos um item");

        verify(productRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_throwProductNotFoundException_when_productNotFound() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        OrderRequestDTO requestDTO = TestDataBuilder.orderRequestDTO(productId, 2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(requestDTO, userId))
                .isInstanceOf(ProductNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_throwUserNotFoundException_when_userNotFound() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderRequestDTO requestDTO = TestDataBuilder.orderRequestDTO(productId, 2);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(requestDTO, userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(productRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_throwValidationException_when_quantityIsInvalid() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Product product = TestDataBuilder.product()
                .id(productId)
                .stockQuantity(10)
                .build();

        OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(productId, 0);
        OrderRequestDTO requestDTO = new OrderRequestDTO(List.of(itemDTO));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> orderService.createOrder(requestDTO, userId))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("maior que zero");

        verify(orderRepository, never()).save(any());
    }

    // ========== GET ORDER BY ID ==========

    @Test
    void should_returnOrder_when_ownedByUser() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Order order = TestDataBuilder.order()
                .id(orderId)
                .user(user)
                .build();
        OrderResponseDTO responseDTO = TestDataBuilder.orderResponseDTO();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toResponseDTO(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.getOrderById(orderId, userId);

        assertThat(result).isNotNull();
        verify(orderRepository).findById(orderId);
        verify(orderMapper).toResponseDTO(order);
    }

    @Test
    void should_throwOrderAccessDeniedException_when_gettingOrderNotOwnedByUser() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User otherUser = TestDataBuilder.user().id(otherUserId).build();
        Order order = TestDataBuilder.order()
                .id(orderId)
                .user(otherUser)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.getOrderById(orderId, userId))
                .isInstanceOf(OrderAccessDeniedException.class)
                .hasMessageContaining(orderId.toString());

        verify(orderMapper, never()).toResponseDTO(any());
    }

    @Test
    void should_throwOrderNotFoundException_when_orderNotFound() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(orderId, userId))
                .isInstanceOf(OrderNotFoundException.class);

        verify(orderMapper, never()).toResponseDTO(any());
    }

    // ========== PAY ORDER ==========

    @Test
    void should_payOrder_when_pendingAndSufficientStock() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Product product = TestDataBuilder.product()
                .id(productId)
                .stockQuantity(10)
                .price(BigDecimal.valueOf(100.00))
                .build();

        OrderItem item = TestDataBuilder.orderItem()
                .product(product)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(200.00))
                .build();

        Order order = TestDataBuilder.order()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PENDENTE)
                .build();
        order.addOrderItem(item);

        Order paidOrder = TestDataBuilder.order()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PAGO)
                .build();
        paidOrder.addOrderItem(item);

        OrderResponseDTO responseDTO = TestDataBuilder.orderResponseDTO();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(paidOrder);
        when(orderMapper.toResponseDTO(paidOrder)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.payOrder(orderId, userId);

        assertThat(result).isNotNull();
        assertThat(product.getStockQuantity()).isEqualTo(8);
        verify(orderRepository).findById(orderId);
        verify(productRepository).saveAll(anyList());
        verify(orderRepository).save(argThat(o -> o.getStatus() == OrderStatus.PAGO));
        verify(orderMapper).toResponseDTO(paidOrder);
    }

    @Test
    void should_throwOrderAlreadyPaidException_when_orderAlreadyPaid() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Order order = TestDataBuilder.order()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PAGO)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.payOrder(orderId, userId))
                .isInstanceOf(OrderAlreadyPaidException.class)
                .hasMessageContaining(orderId.toString())
                .hasMessageContaining("já foi pago");

        verify(productRepository, never()).saveAll(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_throwOrderCancelledException_when_orderCancelled() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Order order = TestDataBuilder.order()
                .id(orderId)
                .user(user)
                .status(OrderStatus.CANCELADO)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.payOrder(orderId, userId))
                .isInstanceOf(OrderCancelledException.class)
                .hasMessageContaining(orderId.toString())
                .hasMessageContaining("cancelado");

        verify(productRepository, never()).saveAll(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_throwOrderAccessDeniedException_when_payingOrderNotOwnedByUser() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User otherUser = TestDataBuilder.user().id(otherUserId).build();
        Order order = TestDataBuilder.order()
                .id(orderId)
                .user(otherUser)
                .status(OrderStatus.PENDENTE)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.payOrder(orderId, userId))
                .isInstanceOf(OrderAccessDeniedException.class)
                .hasMessageContaining(orderId.toString());

        verify(productRepository, never()).saveAll(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_decreaseStock_when_payingOrder() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user = TestDataBuilder.user().id(userId).build();
        Product product = TestDataBuilder.product()
                .id(productId)
                .stockQuantity(10)
                .price(BigDecimal.valueOf(100.00))
                .build();

        OrderItem item = TestDataBuilder.orderItem()
                .product(product)
                .quantity(3)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(300.00))
                .build();

        Order order = TestDataBuilder.order()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PENDENTE)
                .build();
        order.addOrderItem(item);

        Order paidOrder = TestDataBuilder.order()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PAGO)
                .build();
        paidOrder.addOrderItem(item);

        OrderResponseDTO responseDTO = TestDataBuilder.orderResponseDTO();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(paidOrder);
        when(orderMapper.toResponseDTO(paidOrder)).thenReturn(responseDTO);

        orderService.payOrder(orderId, userId);

        assertThat(product.getStockQuantity()).isEqualTo(7);
        verify(productRepository).saveAll(argThat(products -> {
            List<Product> productList = (List<Product>) products;
            return !productList.isEmpty() && productList.get(0).getStockQuantity() == 7;
        }));
    }
}

