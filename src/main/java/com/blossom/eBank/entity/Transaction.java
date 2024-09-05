package com.blossom.eBank.entity;

import com.blossom.eBank.utils.CustomerUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reference_no")
    private String ref;
    @Column(name = "sender_account")
    private String senderAccount;
    @Column(name = "sent_by")
    private String senderName;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "beneficiary_account")
    private String beneficiaryAccount;
    @Column(name = "received_by")
    private String beneficiaryName;
    @Column(name = "trans_status")
    private String status;
    @Column(name = "trans_narration")
    private String narration;
    @CreationTimestamp
    @Column(name = "trans_date")
    private LocalDateTime date;
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @Column(name = "trans_type")
    private String type;
}
