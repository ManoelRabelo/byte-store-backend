package com.bytestore.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 120, message = "O nome do produto deve ter no máximo 120 caracteres.")
        String name,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
        String description,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
        BigDecimal price,

        @NotBlank(message = "A categoria é obrigatória.")
        @Size(max = 60, message = "A categoria deve ter no máximo 60 caracteres.")
        String category,

        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa.")
        Integer stockQuantity) {
}
