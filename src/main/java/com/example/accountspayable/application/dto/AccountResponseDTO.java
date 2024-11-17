package com.example.accountspayable.application.dto;

import com.example.accountspayable.domain.enums.AccountStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AccountResponseDTO {
    private UUID id;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private BigDecimal value;
    private String description;
    private AccountStatus status;
}
