package com.bytestore.repository;

import com.bytestore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserId(UUID userId);

    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Order> findByIdAndUserId(UUID id, UUID userId);

    @Query(value = """
            SELECT 
                BIN_TO_UUID(u.id) AS userID,
                u.name AS userName,
                u.email AS userEmail,
                COUNT(o.id) AS totalOrders,
                COALESCE(SUM(o.total_amount), 0) AS totalAmount
            FROM tb_users u
            INNER JOIN tb_orders o ON u.id = o.user_id
            WHERE o.status = 'PAGO'
            GROUP BY u.id, u.name, u.email
            ORDER BY totalOrders DESC, COALESCE(SUM(o.total_amount), 0) DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Object[]> findTop5UsersByOrders();

    @Query(value = """
            SELECT 
                BIN_TO_UUID(u.id) AS userID,
                u.name AS userName,
                u.email AS userEmail,
                COUNT(o.id) AS totalOrders,
                COALESCE(SUM(o.total_amount), 0) / COUNT(o.id) AS averageTicket
            FROM tb_users u
            LEFT JOIN tb_orders o ON u.id = o.user_id AND o.status = 'PAGO'
            GROUP BY u.id, u.name, u.email
            HAVING COUNT(o.id) > 0
            ORDER BY averageTicket DESC
            """, nativeQuery = true)
    List<Object[]> findAverageTicketByUser();

    @Query(value = """
            SELECT 
                YEAR(o.paid_at) AS year,
                MONTH(o.paid_at) AS month,
                COALESCE(SUM(o.total_amount), 0) AS totalRevenue,
                COUNT(o.id) AS totalOrders
            FROM tb_orders o
            WHERE o.status = 'PAGO'
                AND o.paid_at IS NOT NULL
                AND YEAR(o.paid_at) = :year
                AND MONTH(o.paid_at) = :month
            GROUP BY YEAR(o.paid_at), MONTH(o.paid_at)
            """, nativeQuery = true)
    List<Object[]> findMonthlyRevenue(@Param("year") Integer year, @Param("month") Integer month);
}

