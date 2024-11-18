package com.example.accountspayable.infrastructure.config;

import com.example.accountspayable.domain.entity.AccountEntity;
import com.example.accountspayable.domain.enums.AccountStatus;
import com.example.accountspayable.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OverdueStatusScheduler {

    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOverdueAccounts() {
        List<AccountEntity> dueAccounts = accountRepository.findByStatus(AccountStatus.PENDING);

        LocalDate today = LocalDate.now();
        for (AccountEntity account : dueAccounts) {
            if (account.getDueDate().isBefore(today)) {
                account.setStatus(AccountStatus.OVERDUE);
                accountRepository.save(account);
            }
        }
    }
}
