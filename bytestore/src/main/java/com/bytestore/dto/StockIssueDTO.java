package com.bytestore.dto;

import java.util.UUID;

public record StockIssueDTO(
        UUID productId,
        String productName,
        Integer available,
        Integer requested
) {
}
