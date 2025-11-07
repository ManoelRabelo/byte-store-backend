package com.bytestore.controller;

import com.bytestore.dto.OrderRequestDTO;
import com.bytestore.dto.OrderResponseDTO;
import com.bytestore.dto.OrderSummaryDTO;
import com.bytestore.security.util.AuthenticationUtils;
import com.bytestore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthenticationUtils authenticationUtils;

    public OrderController(OrderService orderService, AuthenticationUtils authenticationUtils) {
        this.orderService = orderService;
        this.authenticationUtils = authenticationUtils;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        UUID userId = authenticationUtils.getAuthenticatedUserId();
        OrderResponseDTO order = orderService.createOrder(request, userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderSummaryDTO>> getUserOrders() {
        UUID userId = authenticationUtils.getAuthenticatedUserId();
        List<OrderSummaryDTO> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID id) {
        UUID userId = authenticationUtils.getAuthenticatedUserId();
        OrderResponseDTO order = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{id}/payment")
    public ResponseEntity<OrderResponseDTO> payOrder(@PathVariable UUID id) {
        UUID userId = authenticationUtils.getAuthenticatedUserId();
        OrderResponseDTO order = orderService.payOrder(id, userId);
        return ResponseEntity.ok(order);
    }
}