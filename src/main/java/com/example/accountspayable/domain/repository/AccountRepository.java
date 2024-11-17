package com.example.accountspayable.domain.repository;

import com.example.accountspayable.domain.entity.AccountEntity;
import com.example.accountspayable.domain.enums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Page<AccountEntity> findByDueDateAndDescription(LocalDate dueDate, String description, Pageable pageable);

    Page<AccountEntity> findByDueDate(LocalDate dueDate, Pageable pageable);

    Page<AccountEntity> findByDescriptionContaining(String description, Pageable pageable);

    List<AccountEntity> findByStatus(AccountStatus status);

    @Query("SELECT SUM(a.value) FROM AccountEntity a WHERE a.paymentDate BETWEEN :startDate AND :endDate AND a.status = 'PAID'")
    Optional<BigDecimal> findTotalPaidBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
