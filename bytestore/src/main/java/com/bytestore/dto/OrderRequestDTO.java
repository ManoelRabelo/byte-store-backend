package com.bytestore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO(
        @NotNull(message = "A lista de itens é obrigatória.")
        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        @Valid
        List<OrderItemRequestDTO> items
) {
}

