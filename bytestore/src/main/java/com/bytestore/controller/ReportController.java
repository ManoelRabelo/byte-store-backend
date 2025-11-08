package com.bytestore.controller;

import com.bytestore.dto.AverageTicketDTO;
import com.bytestore.dto.MonthlyRevenueDTO;
import com.bytestore.dto.TopUsersReportDTO;
import com.bytestore.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-users")
    public ResponseEntity<List<TopUsersReportDTO>> getTop5UsersByOrders() {
        List<TopUsersReportDTO> topUsers = reportService.getTop5UsersByOrders();
        return ResponseEntity.ok(topUsers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/average-ticket")
    public ResponseEntity<List<AverageTicketDTO>> getAverageTicketByUser() {
        List<AverageTicketDTO> averageTickets = reportService.getAverageTicketByUser();
        return ResponseEntity.ok(averageTickets);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly-revenue")
    public ResponseEntity<MonthlyRevenueDTO> getMonthlyRevenue(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        MonthlyRevenueDTO revenue = reportService.getMonthlyRevenue(year, month);
        return ResponseEntity.ok(revenue);
    }
}

