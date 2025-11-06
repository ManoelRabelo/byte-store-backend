package com.bytestore.controller;

import com.bytestore.dto.OrderRequestDTO;
import com.bytestore.dto.OrderResponseDTO;
import com.bytestore.dto.OrderSummaryDTO;
import com.bytestore.entity.User;
import com.bytestore.repository.UserRepository;
import com.bytestore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));

        return user.getId();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        UUID userId = getAuthenticatedUserId();
        OrderResponseDTO order = orderService.createOrder(request, userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderSummaryDTO>> getUserOrders() {
        UUID userId = getAuthenticatedUserId();
        List<OrderSummaryDTO> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        OrderResponseDTO order = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{id}/payment")
    public ResponseEntity<OrderResponseDTO> payOrder(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        OrderResponseDTO order = orderService.payOrder(id, userId);
        return ResponseEntity.ok(order);
    }
}