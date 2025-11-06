package com.bytestore.dto;

import com.bytestore.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        UUID userId,
        OrderStatus status,
        List<OrderItemResponseDTO> items,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
}
