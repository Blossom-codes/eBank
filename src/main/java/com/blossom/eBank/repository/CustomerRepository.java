package com.blossom.eBank.repository;

import com.blossom.eBank.entity.Customer;
import com.blossom.eBank.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
   @Query(value = "SELECT * FROM customers WHERE account_number = ?1", nativeQuery = true)
    Customer existsByAccountNumber(String accountNumber);
    @Query(value = "SELECT * FROM customers WHERE email = ?1", nativeQuery = true)
    Customer existsByEmail(String email);
    @Query(value = "SELECT * FROM customers WHERE phone_number = ?1", nativeQuery = true)
    Customer existsByPhoneNumber(String phoneNumber);
    @Modifying
    @Transactional
    @Query(value = "UPDATE customers c SET c.status = :status WHERE c.id = :id", nativeQuery = true)
    int updateStatus(@Param("status") String status,@Param("id") Long id);

}
