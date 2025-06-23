package com.ff.commandes_service.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private UserRole role;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
