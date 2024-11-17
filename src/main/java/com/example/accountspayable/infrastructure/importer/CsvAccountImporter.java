package com.example.accountspayable.infrastructure.importer;

import com.example.accountspayable.application.dto.AccountRequestDTO;
import com.example.accountspayable.domain.enums.AccountStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvAccountImporter {

    public List<AccountRequestDTO> importFromCsv(MultipartFile file) {
        List<AccountRequestDTO> accounts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                AccountRequestDTO account = AccountRequestDTO.builder()
                        .dueDate(LocalDate.parse(values[0].trim()))
                        .paymentDate(values[1].trim().isEmpty() ? null : LocalDate.parse(values[1].trim()))
                        .value(new BigDecimal(values[2].trim()))
                        .description(values[3].trim())
                        .status(AccountStatus.valueOf(values[4].trim().toUpperCase()))
                        .build();

                accounts.add(account);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }

        return accounts;
    }
}
