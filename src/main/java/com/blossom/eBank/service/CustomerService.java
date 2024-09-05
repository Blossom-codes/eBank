package com.blossom.eBank.service;

import com.blossom.eBank.dto.*;
import com.blossom.eBank.entity.Customer;
import com.blossom.eBank.entity.Transaction;

import java.util.List;

public interface CustomerService {
    ResponseDto createAccount(CustomerRequestDto customerRequestDto);
    ResponseDto login(LoginDto loginDto);
    ResponseDto logout(LoginDto loginDto);
    ResponseDto enquire(EnquiryRequestDto enquiryRequestDto);
    ResponseDto deposit(DepositAndWithdrawDto depositAndWithdrawDto);
    ResponseDto withdraw(DepositAndWithdrawDto depositAndWithdrawDto);
    ResponseDto transfer(TransferDto transferDto);
    Transaction saveTransaction(TransactionDto transactionDto);

    List<Customer> customers();

}
