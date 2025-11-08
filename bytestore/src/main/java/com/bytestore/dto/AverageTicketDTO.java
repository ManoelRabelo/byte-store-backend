package com.bytestore.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AverageTicketDTO(
        UUID userID,
        String userName,
        String userEmail,
        Integer totalOrders,
        BigDecimal averageTicket
) {
}
