package com.ff.commandes_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private List<ImageRequest> images;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageRequest {
        private String url;
        private String title;
    }
}
