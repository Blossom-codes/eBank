package com.blossom.eBank.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String ref;
    private String senderAccount;
    private String senderName;
    private BigDecimal amount;
    private String beneficiaryAccount;
    private String beneficiaryName;
    private String status;
    private String narration;
    private String type;
    private String datetime;
}
