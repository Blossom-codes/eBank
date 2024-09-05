package com.blossom.eBank.controller;

import com.blossom.eBank.dto.*;
import com.blossom.eBank.entity.Customer;
import com.blossom.eBank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eBank/api")
public class BankController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/data")
    public ResponseEntity< List<Customer>> getCustomers()
    {
        return ResponseEntity.ok(customerService.customers());
    }

    @PostMapping("/registration")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerRequestDto customerRequestDto) {
        return ResponseEntity.ok(customerService.createAccount(customerRequestDto));
    }

    // check as put mapping
    @PutMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(customerService.login(loginDto));
    }

    @PutMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestBody LoginDto loginDto) {

        return ResponseEntity.ok(customerService.logout(loginDto));
    }

    @GetMapping("/enquiry")
    public ResponseEntity<ResponseDto> enquiry(@RequestBody Map<String, String> request, @RequestParam String type) {

        EnquiryRequestDto requestDto = EnquiryRequestDto.builder()
                .accountNumber(request.get("account_number"))
                .type(type)
                .build();
        return ResponseEntity.ok(customerService.enquire(requestDto));
    }

    @PutMapping("/deposit")
    public ResponseEntity<ResponseDto> deposit(@RequestBody DepositAndWithdrawDto depositDto) {

        return ResponseEntity.ok(customerService.deposit(depositDto));
    }
    @PutMapping("/withdraw")
    public ResponseEntity<ResponseDto> withdraw(@RequestBody DepositAndWithdrawDto withdrawDto) {

        return ResponseEntity.ok(customerService.withdraw(withdrawDto));
    }
    @PutMapping("/transfer")
    public ResponseEntity<ResponseDto> transfer(@RequestBody TransferDto transferDto) {

        return ResponseEntity.ok(customerService.transfer(transferDto));
    }
}
