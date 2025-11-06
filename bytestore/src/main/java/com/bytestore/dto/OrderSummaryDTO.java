package com.bytestore.dto;

import com.bytestore.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderSummaryDTO(
        UUID id,
        OrderStatus status,
        BigDecimal totalAmount,
        Integer itemCount,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
}
