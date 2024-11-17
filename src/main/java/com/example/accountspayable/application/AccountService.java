package com.example.accountspayable.application;

import com.example.accountspayable.application.dto.AccountRequestDTO;
import com.example.accountspayable.application.dto.AccountResponseDTO;
import com.example.accountspayable.domain.entity.AccountEntity;
import com.example.accountspayable.domain.enums.AccountStatus;
import com.example.accountspayable.domain.repository.AccountRepository;
import com.example.accountspayable.infrastructure.importer.CsvAccountImporter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CsvAccountImporter csvAccountImporter;

    public Page<AccountResponseDTO> getAccounts(LocalDate dueDate, String description, Pageable pageable) {
        if (dueDate != null && description != null) {
            return accountRepository.findByDueDateAndDescription(dueDate, description, pageable)
                    .map(this::mapToResponseDTO);
        } else if (dueDate != null) {
            return accountRepository.findByDueDate(dueDate, pageable)
                    .map(this::mapToResponseDTO);
        } else if (description != null) {
            return accountRepository.findByDescriptionContaining(description, pageable)
                    .map(this::mapToResponseDTO);
        }
        return accountRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    public Optional<AccountResponseDTO> getAccountById(UUID id) {
        return accountRepository.findById(id).map(this::mapToResponseDTO);
    }

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        AccountEntity entity = mapToEntity(accountRequestDTO);
        AccountEntity savedEntity = accountRepository.save(entity);
        return mapToResponseDTO(savedEntity);
    }

    public AccountResponseDTO updateAccount(UUID id, AccountRequestDTO accountRequestDTO) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found");
        }
        AccountEntity entity = mapToEntity(accountRequestDTO);
        entity.setId(id);
        AccountEntity updatedEntity = accountRepository.save(entity);
        return mapToResponseDTO(updatedEntity);
    }

    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found");
        }
        accountRepository.deleteById(id);
    }

    public AccountResponseDTO updateAccountStatus(UUID id, AccountStatus status) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountEntity.setStatus(status);
        AccountEntity updatedEntity = accountRepository.save(accountEntity);
        return mapToResponseDTO(updatedEntity);
    }

    public BigDecimal getTotalPaid(LocalDate startDate, LocalDate endDate) {
        return accountRepository.findTotalPaidBetweenDates(startDate, endDate)
                .orElse(BigDecimal.ZERO);
    }

    public void importAccountsFromCsv(MultipartFile file) {
        List<AccountRequestDTO> accounts = csvAccountImporter.importFromCsv(file);
        accounts.forEach(this::createAccount);
    }

    private AccountEntity mapToEntity(AccountRequestDTO dto) {
        return AccountEntity.builder()
                .dueDate(dto.getDueDate())
                .paymentDate(dto.getPaymentDate())
                .value(dto.getValue())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();
    }

    private AccountResponseDTO mapToResponseDTO(AccountEntity entity) {
        return AccountResponseDTO.builder()
                .id(entity.getId())
                .dueDate(entity.getDueDate())
                .paymentDate(entity.getPaymentDate())
                .value(entity.getValue())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }
}
