package com.ff.commandes_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull(message = "Product ID cannot be blank")
    private Long productId;
    @NotNull(message = "User ID cannot be blank")
    private Long userId;
    @NotNull(message = "Quantity cannot be null")
    private int quantity;
    @NotNull(message = "Total price cannot be null")
    private BigDecimal totalPrice;
    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate;
}
