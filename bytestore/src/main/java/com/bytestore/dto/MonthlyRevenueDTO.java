package com.bytestore.dto;

import java.math.BigDecimal;

public record MonthlyRevenueDTO(
        Integer month,
        Integer year,
        BigDecimal totalRevenue,
        Integer totalOrders
) { }
