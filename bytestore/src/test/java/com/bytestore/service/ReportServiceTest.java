package com.bytestore.service;

import com.bytestore.dto.AverageTicketDTO;
import com.bytestore.dto.MonthlyRevenueDTO;
import com.bytestore.dto.TopUsersReportDTO;
import com.bytestore.exception.ValidationException;
import com.bytestore.mapper.ReportMapper;
import com.bytestore.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportService reportService;

    // ========== GET TOP 5 USERS BY ORDERS ==========

    @Test
    void should_returnTop5Users_when_usersExist() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        
        Object[] result1 = new Object[]{
                userId1.toString(),
                "User 1",
                "user1@teste.com",
                10L,
                BigDecimal.valueOf(1000.00)
        };
        Object[] result2 = new Object[]{
                userId2.toString(),
                "User 2",
                "user2@teste.com",
                5L,
                BigDecimal.valueOf(500.00)
        };
        List<Object[]> queryResults = List.of(result1, result2);
        
        TopUsersReportDTO dto1 = new TopUsersReportDTO(
                userId1,
                "User 1",
                "user1@teste.com",
                10,
                BigDecimal.valueOf(1000.00)
        );
        TopUsersReportDTO dto2 = new TopUsersReportDTO(
                userId2,
                "User 2",
                "user2@teste.com",
                5,
                BigDecimal.valueOf(500.00)
        );
        List<TopUsersReportDTO> expectedDTOs = List.of(dto1, dto2);

        when(orderRepository.findTop5UsersByOrders()).thenReturn(queryResults);
        when(reportMapper.toTopUsersReportDTO(queryResults)).thenReturn(expectedDTOs);

        List<TopUsersReportDTO> result = reportService.getTop5UsersByOrders();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedDTOs);
        verify(orderRepository).findTop5UsersByOrders();
        verify(reportMapper).toTopUsersReportDTO(queryResults);
    }

    @Test
    void should_returnEmptyList_when_noUsersExist() {
        List<Object[]> emptyResults = List.of();
        List<TopUsersReportDTO> emptyDTOs = List.of();
        
        when(orderRepository.findTop5UsersByOrders()).thenReturn(emptyResults);
        when(reportMapper.toTopUsersReportDTO(emptyResults)).thenReturn(emptyDTOs);

        List<TopUsersReportDTO> result = reportService.getTop5UsersByOrders();

        assertThat(result).isEmpty();
        verify(orderRepository).findTop5UsersByOrders();
        verify(reportMapper).toTopUsersReportDTO(emptyResults);
    }

    // ========== GET AVERAGE TICKET BY USER ==========

    @Test
    void should_returnAverageTicketByUser_when_usersExist() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        
        Object[] result1 = new Object[]{
                userId1.toString(),
                "User 1",
                "user1@teste.com",
                10L,
                BigDecimal.valueOf(100.00)
        };
        Object[] result2 = new Object[]{
                userId2.toString(),
                "User 2",
                "user2@teste.com",
                5L,
                BigDecimal.valueOf(50.00)
        };
        List<Object[]> queryResults = List.of(result1, result2);
        
        AverageTicketDTO dto1 = new AverageTicketDTO(
                userId1,
                "User 1",
                "user1@teste.com",
                10,
                BigDecimal.valueOf(100.00)
        );
        AverageTicketDTO dto2 = new AverageTicketDTO(
                userId2,
                "User 2",
                "user2@teste.com",
                5,
                BigDecimal.valueOf(50.00)
        );
        List<AverageTicketDTO> expectedDTOs = List.of(dto1, dto2);

        when(orderRepository.findAverageTicketByUser()).thenReturn(queryResults);
        when(reportMapper.toAverageTicketDTO(queryResults)).thenReturn(expectedDTOs);

        List<AverageTicketDTO> result = reportService.getAverageTicketByUser();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedDTOs);
        verify(orderRepository).findAverageTicketByUser();
        verify(reportMapper).toAverageTicketDTO(queryResults);
    }

    @Test
    void should_returnEmptyList_when_noAverageTicketsExist() {
        List<Object[]> emptyResults = List.of();
        List<AverageTicketDTO> emptyDTOs = List.of();
        
        when(orderRepository.findAverageTicketByUser()).thenReturn(emptyResults);
        when(reportMapper.toAverageTicketDTO(emptyResults)).thenReturn(emptyDTOs);

        List<AverageTicketDTO> result = reportService.getAverageTicketByUser();

        assertThat(result).isEmpty();
        verify(orderRepository).findAverageTicketByUser();
        verify(reportMapper).toAverageTicketDTO(emptyResults);
    }

    // ========== GET MONTHLY REVENUE ==========

    @Test
    void should_returnMonthlyRevenue_when_validYearAndMonth() {
        Integer year = 2025;
        Integer month = 1;
        
        Object[] queryResult = new Object[]{
                year,
                month,
                BigDecimal.valueOf(5000.00),
                25L
        };
        List<Object[]> queryResults = Collections.singletonList(queryResult);
        
        MonthlyRevenueDTO expectedDTO = new MonthlyRevenueDTO(
                month,
                year,
                BigDecimal.valueOf(5000.00),
                25
        );

        when(orderRepository.findMonthlyRevenue(year, month)).thenReturn(queryResults);
        when(reportMapper.toMonthlyRevenueDTO(queryResult)).thenReturn(expectedDTO);

        MonthlyRevenueDTO result = reportService.getMonthlyRevenue(year, month);

        assertThat(result).isNotNull();
        assertThat(result.month()).isEqualTo(month);
        assertThat(result.year()).isEqualTo(year);
        assertThat(result.totalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(5000.00));
        assertThat(result.totalOrders()).isEqualTo(25);
        verify(orderRepository).findMonthlyRevenue(year, month);
        verify(reportMapper).toMonthlyRevenueDTO(queryResult);
    }

    @Test
    void should_returnMonthlyRevenueWithDefaultValues_when_yearAndMonthAreNull() {
        LocalDate now = LocalDate.now();
        Integer expectedYear = now.getYear();
        Integer expectedMonth = now.getMonthValue();
        
        Object[] queryResult = new Object[]{
                expectedYear,
                expectedMonth,
                BigDecimal.valueOf(3000.00),
                15L
        };
        List<Object[]> queryResults = Collections.singletonList(queryResult);
        
        MonthlyRevenueDTO expectedDTO = new MonthlyRevenueDTO(
                expectedMonth,
                expectedYear,
                BigDecimal.valueOf(3000.00),
                15
        );

        when(orderRepository.findMonthlyRevenue(expectedYear, expectedMonth)).thenReturn(queryResults);
        when(reportMapper.toMonthlyRevenueDTO(queryResult)).thenReturn(expectedDTO);

        MonthlyRevenueDTO result = reportService.getMonthlyRevenue(null, null);

        assertThat(result).isNotNull();
        assertThat(result.month()).isEqualTo(expectedMonth);
        assertThat(result.year()).isEqualTo(expectedYear);
        verify(orderRepository).findMonthlyRevenue(expectedYear, expectedMonth);
    }

    @Test
    void should_returnMonthlyRevenueWithDefaultYear_when_yearIsNull() {
        LocalDate now = LocalDate.now();
        Integer expectedYear = now.getYear();
        Integer month = 6;
        
        Object[] queryResult = new Object[]{
                expectedYear,
                month,
                BigDecimal.valueOf(2000.00),
                10L
        };
        List<Object[]> queryResults = Collections.singletonList(queryResult);
        
        MonthlyRevenueDTO expectedDTO = new MonthlyRevenueDTO(
                month,
                expectedYear,
                BigDecimal.valueOf(2000.00),
                10
        );

        when(orderRepository.findMonthlyRevenue(expectedYear, month)).thenReturn(queryResults);
        when(reportMapper.toMonthlyRevenueDTO(queryResult)).thenReturn(expectedDTO);

        MonthlyRevenueDTO result = reportService.getMonthlyRevenue(null, month);

        assertThat(result).isNotNull();
        assertThat(result.month()).isEqualTo(month);
        assertThat(result.year()).isEqualTo(expectedYear);
        verify(orderRepository).findMonthlyRevenue(expectedYear, month);
    }

    @Test
    void should_returnMonthlyRevenueWithDefaultMonth_when_monthIsNull() {
        LocalDate now = LocalDate.now();
        Integer year = 2025;
        Integer expectedMonth = now.getMonthValue();
        
        Object[] queryResult = new Object[]{
                year,
                expectedMonth,
                BigDecimal.valueOf(4000.00),
                20L
        };
        List<Object[]> queryResults = Collections.singletonList(queryResult);
        
        MonthlyRevenueDTO expectedDTO = new MonthlyRevenueDTO(
                expectedMonth,
                year,
                BigDecimal.valueOf(4000.00),
                20
        );

        when(orderRepository.findMonthlyRevenue(year, expectedMonth)).thenReturn(queryResults);
        when(reportMapper.toMonthlyRevenueDTO(queryResult)).thenReturn(expectedDTO);

        MonthlyRevenueDTO result = reportService.getMonthlyRevenue(year, null);

        assertThat(result).isNotNull();
        assertThat(result.month()).isEqualTo(expectedMonth);
        assertThat(result.year()).isEqualTo(year);
        verify(orderRepository).findMonthlyRevenue(year, expectedMonth);
    }

    @Test
    void should_returnZeroRevenue_when_noOrdersInPeriod() {
        Integer year = 2025;
        Integer month = 1;
        List<Object[]> emptyResults = List.of();

        when(orderRepository.findMonthlyRevenue(year, month)).thenReturn(emptyResults);

        MonthlyRevenueDTO result = reportService.getMonthlyRevenue(year, month);

        assertThat(result).isNotNull();
        assertThat(result.month()).isEqualTo(month);
        assertThat(result.year()).isEqualTo(year);
        assertThat(result.totalRevenue()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.totalOrders()).isEqualTo(0);
        verify(orderRepository).findMonthlyRevenue(year, month);
        verify(reportMapper, never()).toMonthlyRevenueDTO(any());
    }

    @Test
    void should_throwValidationException_when_monthIsLessThanOne() {
        Integer year = 2025;
        Integer month = 0;

        assertThatThrownBy(() -> reportService.getMonthlyRevenue(year, month))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("month")
                .hasMessageContaining("entre 1 e 12");

        verify(orderRepository, never()).findMonthlyRevenue(anyInt(), anyInt());
    }

    @Test
    void should_throwValidationException_when_monthIsGreaterThanTwelve() {
        Integer year = 2025;
        Integer month = 13;

        assertThatThrownBy(() -> reportService.getMonthlyRevenue(year, month))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("month")
                .hasMessageContaining("entre 1 e 12");

        verify(orderRepository, never()).findMonthlyRevenue(anyInt(), anyInt());
    }

    @Test
    void should_throwValidationException_when_yearIsLessThan2000() {
        Integer year = 1999;
        Integer month = 1;

        assertThatThrownBy(() -> reportService.getMonthlyRevenue(year, month))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("year")
                .hasMessageContaining("entre 2000 e 2999");

        verify(orderRepository, never()).findMonthlyRevenue(anyInt(), anyInt());
    }

    @Test
    void should_throwValidationException_when_yearIsGreaterThan2999() {
        Integer year = 3000;
        Integer month = 1;

        assertThatThrownBy(() -> reportService.getMonthlyRevenue(year, month))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("year")
                .hasMessageContaining("entre 2000 e 2999");

        verify(orderRepository, never()).findMonthlyRevenue(anyInt(), anyInt());
    }

    @Test
    void should_acceptValidMonthBoundaries() {
        Integer year = 2025;
        
        Object[] queryResult1 = new Object[]{
                year,
                1,
                BigDecimal.valueOf(1000.00),
                5L
        };
        Object[] queryResult12 = new Object[]{
                year,
                12,
                BigDecimal.valueOf(2000.00),
                10L
        };
        
        List<Object[]> queryResults1 = Collections.singletonList(queryResult1);
        List<Object[]> queryResults12 = Collections.singletonList(queryResult12);
        
        MonthlyRevenueDTO dto1 = new MonthlyRevenueDTO(1, year, BigDecimal.valueOf(1000.00), 5);
        MonthlyRevenueDTO dto12 = new MonthlyRevenueDTO(12, year, BigDecimal.valueOf(2000.00), 10);

        when(orderRepository.findMonthlyRevenue(year, 1)).thenReturn(queryResults1);
        when(orderRepository.findMonthlyRevenue(year, 12)).thenReturn(queryResults12);
        when(reportMapper.toMonthlyRevenueDTO(queryResult1)).thenReturn(dto1);
        when(reportMapper.toMonthlyRevenueDTO(queryResult12)).thenReturn(dto12);

        MonthlyRevenueDTO result1 = reportService.getMonthlyRevenue(year, 1);
        MonthlyRevenueDTO result12 = reportService.getMonthlyRevenue(year, 12);

        assertThat(result1.month()).isEqualTo(1);
        assertThat(result12.month()).isEqualTo(12);
        verify(orderRepository).findMonthlyRevenue(year, 1);
        verify(orderRepository).findMonthlyRevenue(year, 12);
    }

    @Test
    void should_acceptValidYearBoundaries() {
        Integer month = 6;
        
        Object[] queryResult2000 = new Object[]{
                2000,
                month,
                BigDecimal.valueOf(1000.00),
                5L
        };
        Object[] queryResult2999 = new Object[]{
                2999,
                month,
                BigDecimal.valueOf(2000.00),
                10L
        };
        
        List<Object[]> queryResults2000 = Collections.singletonList(queryResult2000);
        List<Object[]> queryResults2999 = Collections.singletonList(queryResult2999);
        
        MonthlyRevenueDTO dto2000 = new MonthlyRevenueDTO(month, 2000, BigDecimal.valueOf(1000.00), 5);
        MonthlyRevenueDTO dto2999 = new MonthlyRevenueDTO(month, 2999, BigDecimal.valueOf(2000.00), 10);

        when(orderRepository.findMonthlyRevenue(2000, month)).thenReturn(queryResults2000);
        when(orderRepository.findMonthlyRevenue(2999, month)).thenReturn(queryResults2999);
        when(reportMapper.toMonthlyRevenueDTO(queryResult2000)).thenReturn(dto2000);
        when(reportMapper.toMonthlyRevenueDTO(queryResult2999)).thenReturn(dto2999);

        MonthlyRevenueDTO result2000 = reportService.getMonthlyRevenue(2000, month);
        MonthlyRevenueDTO result2999 = reportService.getMonthlyRevenue(2999, month);

        assertThat(result2000.year()).isEqualTo(2000);
        assertThat(result2999.year()).isEqualTo(2999);
        verify(orderRepository).findMonthlyRevenue(2000, month);
        verify(orderRepository).findMonthlyRevenue(2999, month);
    }
}

