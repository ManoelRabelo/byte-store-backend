package com.bytestore.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TopUsersReportDTO(
        UUID userID,
        String userName,
        String userEmail,
        Integer totalOrders,
        BigDecimal totalAmount
) {
}
