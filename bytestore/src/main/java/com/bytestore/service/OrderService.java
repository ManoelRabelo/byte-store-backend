package com.bytestore.service;

import com.bytestore.dto.*;
import com.bytestore.entity.*;
import com.bytestore.exception.*;
import com.bytestore.mapper.OrderMapper;
import com.bytestore.repository.OrderRepository;
import com.bytestore.repository.ProductRepository;
import com.bytestore.repository.UserRepository;
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
            throw new OrderAccessDeniedException(order.getId());
        }
    }

    @Transactional(noRollbackFor = OrderCancelledDueToStockException.class)
    public OrderResponseDTO createOrder(OrderRequestDTO dto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (dto.items() == null || dto.items().isEmpty()) {
            throw new ValidationException("items", "O pedido deve conter pelo menos um item.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        List<StockIssueDTO> stockIssues = new ArrayList<>();

        for (OrderItemRequestDTO itemDTO : dto.items()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new ProductNotFoundException(itemDTO.productId()));

            if (itemDTO.quantity() == null || itemDTO.quantity() <= 0) {
                throw new ValidationException("quantity", "A quantidade deve ser maior que zero.");
            }

            if (!product.hasStock(itemDTO.quantity())) {
                stockIssues.add(new StockIssueDTO(
                        product.getId(),
                        product.getName(),
                        product.getStockQuantity(),
                        itemDTO.quantity()
                ));
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.quantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity())));
            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .user(user)
                .orderItems(new ArrayList<>())
                .status(stockIssues.isEmpty() ? OrderStatus.PENDENTE : OrderStatus.CANCELADO)
                .build();

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        order.calculateTotalAmount();
        Order savedOrder = orderRepository.save(order);

        if (!stockIssues.isEmpty()) {
            throw new OrderCancelledDueToStockException(savedOrder, stockIssues);
        }

        return orderMapper.toResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryDTO> getUserOrders(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return orders.stream()
                .map(orderMapper::toSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateOrderOwnership(order, userId);

        return orderMapper.toResponseDTO(order);
    }

    @Transactional(noRollbackFor = OrderCancelledDueToStockException.class)
    public OrderResponseDTO payOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateOrderOwnership(order, userId);

        if (order.getStatus() == OrderStatus.PAGO) {
            throw new OrderAlreadyPaidException(order.getId());
        }

        if (order.getStatus() == OrderStatus.CANCELADO) {
            throw new OrderCancelledException(order.getId());
        }

        List<Product> productsToUpdate = new ArrayList<>();
        List<StockIssueDTO> stockIssues = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();

            if (!product.hasStock(item.getQuantity())) {
                stockIssues.add(new StockIssueDTO(
                        product.getId(),
                        product.getName(),
                        product.getStockQuantity(),
                        item.getQuantity()
                ));
            } else {
                product.decreaseStock(item.getQuantity());
                productsToUpdate.add(product);
            }
        }

        if (!stockIssues.isEmpty()) {
            order.setStatus(OrderStatus.CANCELADO);
            Order cancelledOrder = orderRepository.save(order);
            throw new OrderCancelledDueToStockException(cancelledOrder, stockIssues);
        }

        productRepository.saveAll(productsToUpdate);

        order.setStatus(OrderStatus.PAGO);
        order.setPaidAt(LocalDateTime.now());

        Order paidOrder = orderRepository.save(order);

        return orderMapper.toResponseDTO(paidOrder);
    }
}
