package com.blossom.eBank.dto;

import com.blossom.eBank.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDto {
    private String accountNumber;
    private String accountName;
    private BigDecimal accountBalance;
    private String accountStatus;
}
