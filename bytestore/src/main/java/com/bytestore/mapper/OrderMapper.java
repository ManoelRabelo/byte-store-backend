package com.bytestore.mapper;

import com.bytestore.dto.OrderItemResponseDTO;
import com.bytestore.dto.OrderResponseDTO;
import com.bytestore.dto.OrderSummaryDTO;
import com.bytestore.entity.Order;
import com.bytestore.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDTO toResponseDTO(Order order) {
        List<OrderItemResponseDTO> itemsDTO = order.getOrderItems().stream()
                .map(this::toItemResponseDTO)
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                itemsDTO,
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getPaidAt());
    }

    public OrderSummaryDTO toSummaryDTO(Order order) {
        Integer itemCount = order.getOrderItems() != null ? order.getOrderItems().size() : 0;

        return new OrderSummaryDTO(
                order.getId(),
                order.getStatus(),
                order.getTotalAmount(),
                itemCount,
                order.getCreatedAt(),
                order.getPaidAt());
    }

    public OrderItemResponseDTO toItemResponseDTO(OrderItem item) {
        return new OrderItemResponseDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal());
    }
}

