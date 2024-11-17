package com.example.accountspayable.presentation;

import com.example.accountspayable.application.AccountService;
import com.example.accountspayable.application.dto.AccountRequestDTO;
import com.example.accountspayable.application.dto.AccountResponseDTO;
import com.example.accountspayable.domain.enums.AccountStatus;
import com.example.accountspayable.presentation.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<AccountResponseDTO>>> getAccounts(
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(required = false) String description,
            Pageable pageable) {
        Page<AccountResponseDTO> accounts = accountService.getAccounts(dueDate, description, pageable);
        if (accounts.isEmpty()) {
            return ResponseEntity.ok(
                    ResponseDTO.<Page<AccountResponseDTO>>builder()
                            .status("error")
                            .message("No accounts found")
                            .data(null)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseDTO.<Page<AccountResponseDTO>>builder()
                        .status("success")
                        .message("Accounts retrieved successfully")
                        .data(accounts)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AccountResponseDTO>> getAccountById(@PathVariable UUID id) {
        AccountResponseDTO account = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return ResponseEntity.ok(
                ResponseDTO.<AccountResponseDTO>builder()
                        .status("success")
                        .message("Account retrieved successfully")
                        .data(account)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<AccountResponseDTO>> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        AccountResponseDTO createdAccount = accountService.createAccount(accountRequestDTO);
        return ResponseEntity.ok(
                ResponseDTO.<AccountResponseDTO>builder()
                        .status("success")
                        .message("Account created successfully")
                        .data(createdAccount)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<AccountResponseDTO>> updateAccount(
            @PathVariable UUID id, @RequestBody AccountRequestDTO accountRequestDTO) {
        AccountResponseDTO updatedAccount = accountService.updateAccount(id, accountRequestDTO);
        return ResponseEntity.ok(
                ResponseDTO.<AccountResponseDTO>builder()
                        .status("success")
                        .message("Account updated successfully")
                        .data(updatedAccount)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(
                ResponseDTO.<Void>builder()
                        .status("success")
                        .message("Account deleted successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<AccountResponseDTO>> updateAccountStatus(
            @PathVariable UUID id, @RequestParam AccountStatus status) {
        AccountResponseDTO updatedAccount = accountService.updateAccountStatus(id, status);
        return ResponseEntity.ok(
                ResponseDTO.<AccountResponseDTO>builder()
                        .status("success")
                        .message("Account status updated successfully")
                        .data(updatedAccount)
                        .build()
        );
    }

    @GetMapping("/total-paid")
    public ResponseEntity<ResponseDTO<BigDecimal>> getTotalPaid(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        BigDecimal totalPaid = accountService.getTotalPaid(startDate, endDate);
        return ResponseEntity.ok(
                ResponseDTO.<BigDecimal>builder()
                        .status("success")
                        .message("Total paid amount retrieved successfully")
                        .data(totalPaid)
                        .build()
        );
    }

    @PostMapping("/import")
    public String importAccounts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        accountService.importAccountsFromCsv(file);
        return "Accounts imported successfully!";
    }
}
