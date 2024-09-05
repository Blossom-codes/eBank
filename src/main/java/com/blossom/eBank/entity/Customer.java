package com.blossom.eBank.entity;

import com.blossom.eBank.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private BigDecimal balance;
    @Column(name = "account_number")
    private String accountNumber;
    private String email;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String status;
    @CreationTimestamp
    @Column(name = "date_joined")
    private LocalDateTime dateJoined;
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}
