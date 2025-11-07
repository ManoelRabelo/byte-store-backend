package com.bytestore.service;

import com.bytestore.dto.*;
import com.bytestore.entity.*;
import com.bytestore.mapper.OrderMapper;
import com.bytestore.repository.OrderRepository;
import com.bytestore.repository.ProductRepository;
import com.bytestore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    private void validateOrderOwnership(Order order, UUID userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Este pedido não pertence ao usuário autenticado.");
        }
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (dto.items() == null || dto.items().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        boolean stockAvailable = true;
        StringBuilder stockErrors = new StringBuilder();

        for (OrderItemRequestDTO itemDTO : dto.items()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Produto com ID " + itemDTO.productId() + " não encontrado."));

            if (itemDTO.quantity() == null || itemDTO.quantity() <= 0) {
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            }

            if (!product.hasStock(itemDTO.quantity())) {
                stockAvailable = false;
                stockErrors
                        .append(String.format("Produto '%s': estoque insuficiente (disponível: %d, solicitado: %d). ",
                                product.getName(), product.getStockQuantity(), itemDTO.quantity()));
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.quantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity())));

            orderItems.add(orderItem);
        }

        if (!stockAvailable) {
            throw new IllegalArgumentException("Estoque insuficiente: " + stockErrors.toString());
        }

        Order order = Order.builder()
                .user(user)
                .orderItems(new ArrayList<>())
                .status(OrderStatus.PENDENTE)
                .build();

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        order.calculateTotalAmount();

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryDTO> getUserOrders(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return orders.stream()
                .map(orderMapper::toSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        validateOrderOwnership(order, userId);

        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO payOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        validateOrderOwnership(order, userId);

        if (order.getStatus() == OrderStatus.PAGO) {
            throw new IllegalArgumentException("Este pedido já foi pago.");
        }

        if (order.getStatus() == OrderStatus.CANCELADO) {
            throw new IllegalArgumentException("Não é possível pagar um pedido cancelado.");
        }

        List<Product> productsToUpdate = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();

            if (!product.hasStock(item.getQuantity())) {
                throw new IllegalArgumentException(
                        String.format("Estoque insuficiente para o produto '%s'. Disponível: %d, Necessário: %d",
                                product.getName(), product.getStockQuantity(), item.getQuantity()));
            }

            product.decreaseStock(item.getQuantity());
            productsToUpdate.add(product);
        }

        productRepository.saveAll(productsToUpdate);

        order.setStatus(OrderStatus.PAGO);
        order.setPaidAt(LocalDateTime.now());

        Order paidOrder = orderRepository.save(order);

        return orderMapper.toResponseDTO(paidOrder);
    }
}
