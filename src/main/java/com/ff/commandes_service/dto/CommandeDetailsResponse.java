package com.ff.commandes_service.dto;

import com.ff.commandes_service.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeDetailsResponse {
    Long id;
    private Long clientId;
    private Long productId;
    private ProductResponseDto product;
    private ClientResponseDto client;

    public CommandeDetailsResponse(Orders orders, ProductResponseDto product, ClientResponseDto clientDetails) {
    }
}
