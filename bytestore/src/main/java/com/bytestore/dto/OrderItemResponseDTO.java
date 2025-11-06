package com.bytestore.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID id,
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
