package com.example.accountspayable.application;

import com.example.accountspayable.application.dto.AccountRequestDTO;
import com.example.accountspayable.application.dto.AccountResponseDTO;
import com.example.accountspayable.domain.entity.AccountEntity;
import com.example.accountspayable.domain.enums.AccountStatus;
import com.example.accountspayable.domain.repository.AccountRepository;
import com.example.accountspayable.infrastructure.importer.CsvAccountImporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CsvAccountImporter csvAccountImporter;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testGetAccounts() {
        PageRequest pageable = PageRequest.of(0, 10);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(UUID.randomUUID());
        accountEntity.setDueDate(LocalDate.of(2024, 11, 1));
        accountEntity.setPaymentDate(null);
        accountEntity.setValue(new BigDecimal("500.00"));
        accountEntity.setDescription("Conta de eletricidade");
        accountEntity.setStatus(AccountStatus.PENDING);

        Page<AccountEntity> page = new PageImpl<>(List.of(accountEntity));
        when(accountRepository.findAll(pageable)).thenReturn(page);

        Page<AccountResponseDTO> result = accountService.getAccounts(null, null, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("Conta de eletricidade", result.getContent().get(0).getDescription());

        verify(accountRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAccountById() {
        UUID id = UUID.randomUUID();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setDueDate(LocalDate.of(2024, 11, 1));
        accountEntity.setDescription("Conta de eletricidade");
        accountEntity.setStatus(AccountStatus.PENDING);

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountEntity));

        AccountResponseDTO result = accountService.getAccountById(id).orElseThrow();
        assertEquals("Conta de eletricidade", result.getDescription());

        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    void testCreateAccount() {
        AccountRequestDTO requestDTO = AccountRequestDTO.builder()
                .dueDate(LocalDate.of(2024, 11, 1))
                .value(new BigDecimal("500.00"))
                .description("Conta de eletricidade")
                .status(AccountStatus.PENDING)
                .build();

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(UUID.randomUUID());
        accountEntity.setDueDate(requestDTO.getDueDate());
        accountEntity.setValue(requestDTO.getValue());
        accountEntity.setDescription(requestDTO.getDescription());
        accountEntity.setStatus(requestDTO.getStatus());

        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        AccountResponseDTO result = accountService.createAccount(requestDTO);
        assertEquals("Conta de eletricidade", result.getDescription());

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    void testUpdateAccount() {
        UUID id = UUID.randomUUID();
        AccountRequestDTO requestDTO = AccountRequestDTO.builder()
                .dueDate(LocalDate.of(2024, 11, 1))
                .value(new BigDecimal("500.00"))
                .description("Conta de eletricidade atualizada")
                .status(AccountStatus.PAID)
                .build();

        when(accountRepository.existsById(id)).thenReturn(true);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setDueDate(requestDTO.getDueDate());
        accountEntity.setValue(requestDTO.getValue());
        accountEntity.setDescription(requestDTO.getDescription());
        accountEntity.setStatus(requestDTO.getStatus());

        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        AccountResponseDTO result = accountService.updateAccount(id, requestDTO);
        assertEquals("Conta de eletricidade atualizada", result.getDescription());

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    void testDeleteAccount() {
        UUID id = UUID.randomUUID();
        when(accountRepository.existsById(id)).thenReturn(true);

        accountService.deleteAccount(id);

        verify(accountRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdateAccountStatus() {
        UUID id = UUID.randomUUID();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setStatus(AccountStatus.PENDING);

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountEntity));
        when(accountRepository.save(accountEntity)).thenReturn(accountEntity);

        AccountResponseDTO result = accountService.updateAccountStatus(id, AccountStatus.PAID);
        assertEquals(AccountStatus.PAID, result.getStatus());

        verify(accountRepository, times(1)).save(accountEntity);
    }

    @Test
    void testImportAccountsFromCsv() {
        MultipartFile file = mock(MultipartFile.class);

        AccountRequestDTO accountRequestDTO = AccountRequestDTO.builder()
                .dueDate(LocalDate.of(2024, 11, 1))
                .value(new BigDecimal("500.00"))
                .description("Conta de eletricidade")
                .status(AccountStatus.PENDING)
                .build();

        when(csvAccountImporter.importFromCsv(file)).thenReturn(List.of(accountRequestDTO));

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(UUID.randomUUID());
        accountEntity.setDueDate(accountRequestDTO.getDueDate());
        accountEntity.setValue(accountRequestDTO.getValue());
        accountEntity.setDescription(accountRequestDTO.getDescription());
        accountEntity.setStatus(accountRequestDTO.getStatus());

        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        accountService.importAccountsFromCsv(file);

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

}
