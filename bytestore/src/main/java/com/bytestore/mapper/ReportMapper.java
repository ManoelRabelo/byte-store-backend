package com.bytestore.mapper;

import com.bytestore.dto.AverageTicketDTO;
import com.bytestore.dto.MonthlyRevenueDTO;
import com.bytestore.dto.TopUsersReportDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ReportMapper {

    public List<TopUsersReportDTO> toTopUsersReportDTO(List<Object[]> results) {
        List<TopUsersReportDTO> dtos = new ArrayList<>();
        
        for (Object[] row : results) {
            UUID userID = UUID.fromString((String) row[0]);
            String userName = (String) row[1];
            String userEmail = (String) row[2];
            Long totalOrdersLong = ((Number) row[3]).longValue();
            Integer totalOrders = totalOrdersLong.intValue();
            BigDecimal totalAmount = ((BigDecimal) row[4]);

            dtos.add(new TopUsersReportDTO(
                    userID,
                    userName,
                    userEmail,
                    totalOrders,
                    totalAmount
            ));
        }

        return dtos;
    }

    public List<AverageTicketDTO> toAverageTicketDTO(List<Object[]> results) {
        List<AverageTicketDTO> dtos = new ArrayList<>();
        
        for (Object[] row : results) {
            UUID userID = UUID.fromString((String) row[0]);
            String userName = (String) row[1];
            String userEmail = (String) row[2];
            Long totalOrdersLong = ((Number) row[3]).longValue();
            Integer totalOrders = totalOrdersLong.intValue();
            BigDecimal averageTicket = ((BigDecimal) row[4]);

            dtos.add(new AverageTicketDTO(
                    userID,
                    userName,
                    userEmail,
                    totalOrders,
                    averageTicket
            ));
        }

        return dtos;
    }

    public MonthlyRevenueDTO toMonthlyRevenueDTO(Object[] row) {
        Integer year = ((Number) row[0]).intValue();
        Integer month = ((Number) row[1]).intValue();
        BigDecimal totalRevenue = ((BigDecimal) row[2]);
        Long totalOrdersLong = ((Number) row[3]).longValue();
        Integer totalOrders = totalOrdersLong.intValue();

        return new MonthlyRevenueDTO(
                month,
                year,
                totalRevenue,
                totalOrders
        );
    }
}

