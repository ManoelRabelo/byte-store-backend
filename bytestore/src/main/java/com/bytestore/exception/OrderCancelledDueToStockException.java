package com.bytestore.exception;

import com.bytestore.dto.StockIssueDTO;
import com.bytestore.entity.Order;

import java.util.List;

public class OrderCancelledDueToStockException extends BaseException {

    private final Order cancelledOrder;
    private final List<StockIssueDTO> stockIssues;

    public OrderCancelledDueToStockException(Order cancelledOrder, List<StockIssueDTO> stockIssues) {
        super(buildMessage(stockIssues));
        this.cancelledOrder = cancelledOrder;
        this.stockIssues = stockIssues;
    }

    public static String buildMessage(List<StockIssueDTO> stockIssues) {
        if (stockIssues.isEmpty()){
            return "Pedido cancelado devido à falta de estoque.";
        }

        StringBuilder message = new StringBuilder("Pedido cancelado. Produtos sem estoque suficiente: ");

        for (int i = 0; i < stockIssues.size(); i++) {
            StockIssueDTO issue = stockIssues.get(i);
            message.append(String.format("%s (disponível: %d, solicitado: %d)",
                    issue.productName(), issue.available(), issue.requested()));
            if (i < stockIssues.size() - 1) {
                message.append("; ");
            }
        }

        return message.toString();
    }
}

