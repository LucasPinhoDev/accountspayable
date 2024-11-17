package com.example.accountspayable.application.dto;

import com.example.accountspayable.domain.enums.AccountStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class AccountRequestDTO {
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private BigDecimal value;
    private String description;
    private AccountStatus status;
}
