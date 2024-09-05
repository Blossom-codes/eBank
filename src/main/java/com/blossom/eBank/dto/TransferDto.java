package com.blossom.eBank.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    private String senderAccount;
    private String beneficiaryAccount;
    private BigDecimal amount;
    private String password;
    private String transNarration;
}
