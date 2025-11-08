package com.bytestore.service;

import com.bytestore.dto.AverageTicketDTO;
import com.bytestore.dto.MonthlyRevenueDTO;
import com.bytestore.dto.TopUsersReportDTO;
import com.bytestore.exception.ValidationException;
import com.bytestore.mapper.ReportMapper;
import com.bytestore.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final ReportMapper reportMapper;

    public ReportService(OrderRepository orderRepository, ReportMapper reportMapper) {
        this.orderRepository = orderRepository;
        this.reportMapper = reportMapper;
    }

    @Transactional(readOnly = true)
    public List<TopUsersReportDTO> getTop5UsersByOrders() {
        List<Object[]> results = orderRepository.findTop5UsersByOrders();
        return reportMapper.toTopUsersReportDTO(results);
    }

    @Transactional(readOnly = true)
    public List<AverageTicketDTO> getAverageTicketByUser() {
        List<Object[]> results = orderRepository.findAverageTicketByUser();
        return reportMapper.toAverageTicketDTO(results);
    }

    @Transactional(readOnly = true)
    public MonthlyRevenueDTO getMonthlyRevenue(Integer year, Integer month) {
        LocalDate now = LocalDate.now();
        year = (year != null) ? year : now.getYear();
        month = (month != null) ? month : now.getMonthValue();

        if (month < 1 || month > 12) {
            throw new ValidationException("month", "O mês deve estar entre 1 e 12.");
        }

        if (year < 2000 || year > 2999) {
            throw new ValidationException("year", "O ano deve estar entre 2000 e 2999.");
        }

        List<Object[]> results = orderRepository.findMonthlyRevenue(year, month);

        if (results.isEmpty()) {
            return new MonthlyRevenueDTO(
                    month,
                    year,
                    BigDecimal.ZERO,
                    0
            );
        }

        Object[] result = results.get(0);
        return reportMapper.toMonthlyRevenueDTO(result);
    }
}

