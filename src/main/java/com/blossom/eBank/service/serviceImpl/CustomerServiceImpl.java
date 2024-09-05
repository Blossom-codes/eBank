package com.blossom.eBank.service.serviceImpl;

import com.blossom.eBank.dto.*;
import com.blossom.eBank.entity.Customer;
import com.blossom.eBank.entity.Transaction;
import com.blossom.eBank.repository.CustomerRepository;
import com.blossom.eBank.repository.TransactionRepository;
import com.blossom.eBank.service.CustomerService;
import com.blossom.eBank.service.EmailService;
import com.blossom.eBank.utils.CustomerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public ResponseDto createAccount(CustomerRequestDto customerRequestDto) {
        String accountNumber = new CustomerUtils().generateAccountNumber();
        try {
            if (customerRepository.existsByAccountNumber(accountNumber) != null) {
                accountNumber = new CustomerUtils().generateAccountNumber();
            }
            if (customerRepository.existsByEmail(customerRequestDto.getEmail()) != null) {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.ACCOUNT_EXISTS_CODE)
                        .responseMessage(CustomerUtils.ACCOUNT_EMAIL_EXISTS_MESSAGE)
                        .info(null)
                        .build();
            }
            if (customerRepository.existsByPhoneNumber(customerRequestDto.getPhoneNumber()) != null) {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.ACCOUNT_EXISTS_CODE)
                        .responseMessage(CustomerUtils.ACCOUNT_PHONE_NUMBER_EXISTS_MESSAGE)
                        .info(null)
                        .build();
            }


            Customer newCustomer = Customer.builder()
                    .firstName(customerRequestDto.getFirstName())
                    .lastName(customerRequestDto.getLastName())
                    .balance(BigDecimal.ZERO)
                    .accountNumber(accountNumber)
                    .email(customerRequestDto.getEmail())
                    .password(new CustomerUtils().hashingInput(customerRequestDto.getPassword()))
                    .phoneNumber(customerRequestDto.getPhoneNumber())
                    .status(CustomerUtils.NOTLOGGEDIN)
                    .build();
            Customer saved = customerRepository.save(newCustomer);

            EmailDto emailDto = EmailDto.builder()
                    .recipient(saved.getEmail())
                    .subject("eBANK ACCOUNT CREATION")
                    .message("Welcome " + saved.getFirstName() + ", Your account has been successfully created!\n" +
                            "Your account details are below:\n" +
                            "Account Name: " + saved.getFirstName() + " " + saved.getLastName() + "\n" +
                            "Account number: " + saved.getAccountNumber() + "\n" +
                            "Login to activate your account")
                    .build();
            emailService.sendEmailAlert(emailDto);
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.SUCCESS_CODE)
                    .responseMessage(CustomerUtils.SUCCESS_MESSAGE)
                    .info(AccountInfoDto.builder()
                            .accountName(saved.getFirstName() + " " + saved.getLastName())
                            .accountBalance(saved.getBalance())
                            .accountNumber(saved.getAccountNumber())
                            .accountStatus(saved.getStatus())
                            .build())
                    .build();
        } catch (Exception e) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.EXCEPTION_CODE)
                    .responseMessage(CustomerUtils.EXCEPTION_MESSAGE + e)
                    .info(null)
                    .build();
        }


    }

    @Override
    public ResponseDto login(LoginDto loginDto) {
        Customer user = customerRepository.existsByEmail(loginDto.getEmail());
        try {
            if (Objects.equals(user.getEmail(), loginDto.getEmail())) {

                if (Objects.equals(user.getPassword(), new CustomerUtils().hashingInput(loginDto.getPassword()))) {
                    user.setStatus(CustomerUtils.LOGGEDIN);
                    customerRepository.save(user);
                    return ResponseDto.builder()
                            .responseCode(CustomerUtils.LOGIN_SUCCESS_CODE)
                            .responseMessage(CustomerUtils.LOGIN_WELCOME_MESSAGE)
                            .info(AccountInfoDto.builder()
                                    .accountName(user.getFirstName() + " " + user.getLastName())
                                    .accountNumber(user.getAccountNumber())
                                    .accountBalance(user.getBalance())
                                    .accountStatus(user.getStatus())
                                    .build())
                            .build();


                }
            }
        } catch (Exception ex) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.EXCEPTION_CODE)
                    .responseMessage(CustomerUtils.EXCEPTION_MESSAGE + ex)
                    .info(null)
                    .build();
        }
        return ResponseDto.builder()
                .responseCode(CustomerUtils.LOGIN_ERROR_CODE)
                .responseMessage(CustomerUtils.LOGIN_ERROR_MESSAGE)
                .info(null)
                .build();
    }

    @Override
    public ResponseDto logout(LoginDto loginDto) {
        Customer user = customerRepository.existsByEmail(loginDto.getEmail());
        if (!user.getStatus().equals(CustomerUtils.LOGGEDIN)) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.AUTHORIZATION_ERROR_CODE)
                    .responseMessage(CustomerUtils.AUTHORIZATION_ERROR_MESSAGE)
                    .build();
        }
        if (Objects.equals(user.getEmail(), loginDto.getEmail())) {
            user.setStatus(CustomerUtils.NOTLOGGEDIN);
            customerRepository.save(user);
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.LOGIN_SUCCESS_CODE)
                    .responseMessage(CustomerUtils.LOGIN_EXIT_MESSAGE)
                    .info(null)
                    .build();
        }


        return ResponseDto.builder()
                .responseCode(CustomerUtils.LOGOUT_ERROR_CODE)
                .responseMessage(CustomerUtils.EXCEPTION_MESSAGE)
                .info(null)
                .build();
    }

    @Override
    public ResponseDto enquire(EnquiryRequestDto enquiryRequestDto) {
        try {
            Customer user = customerRepository.existsByAccountNumber(enquiryRequestDto.getAccountNumber());
            if (user == null) {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                        .responseMessage(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE_MESSAGE)
                        .info(null)
                        .build();
            }
            if (!user.getStatus().equals(CustomerUtils.LOGGEDIN)) {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.AUTHORIZATION_ERROR_CODE)
                        .responseMessage(CustomerUtils.AUTHORIZATION_ERROR_MESSAGE)
                        .build();
            }
            if (Objects.equals(enquiryRequestDto.getType(), "balance")) {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.BALANCE_ENQUIRY_SUCCESS)
                        .responseMessage(CustomerUtils.BALANCE_ENQUIRY_MESSAGE + user.getBalance())
                        .build();
            } else if (Objects.equals(enquiryRequestDto.getType(), "name")) {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.NAME_ENQUIRY_SUCCESS)
                        .responseMessage(CustomerUtils.NAME_ENQUIRY_MESSAGE + user.getFirstName() + " " + user.getLastName())
                        .build();
            } else {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.ENQUIRY_ERROR_CODE)
                        .responseMessage(CustomerUtils.ENQUIRY_ERROR_MESSAGE)
                        .info(null)
                        .build();
            }
        } catch (Exception e) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.EXCEPTION_CODE)
                    .responseMessage(CustomerUtils.EXCEPTION_MESSAGE + e)
                    .info(null)
                    .build();
        }
    }

    @Override
    public ResponseDto deposit(DepositAndWithdrawDto depositAndWithdrawDto) {
        Customer user = customerRepository.existsByAccountNumber(depositAndWithdrawDto.getAccountNumber());

        if (user == null) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE_MESSAGE)
                    .build();
        }
        if (!user.getStatus().equals(CustomerUtils.LOGGEDIN)) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.AUTHORIZATION_ERROR_CODE)
                    .responseMessage(CustomerUtils.AUTHORIZATION_ERROR_MESSAGE)
                    .build();
        }
        if (depositAndWithdrawDto.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.INVALID_AMOUNT_CODE)
                    .responseMessage(CustomerUtils.INVALID_AMOUNT_MESSAGE)
                    .build();
        }


        user.setBalance(user.getBalance().add(depositAndWithdrawDto.getAmount()));
        customerRepository.save(user);
        emailService.sendEmailAlert(EmailDto.builder()
                .subject(CustomerUtils.CREDIT + " ALERT!")
                .recipient(user.getEmail())
                .message("TRANSACTION RECEIPT BELOW: \n" +
                        TransactionDto.builder()
                                .senderName(user.getFirstName() + " " + user.getLastName())
                                .senderAccount(Year.now() + "XXXXXX")
                                .amount(depositAndWithdrawDto.getAmount())
                                .type(CustomerUtils.CREDIT)
                                .build().toString())
                .build());
        return ResponseDto.builder()
                .responseCode(CustomerUtils.SUCCESS_CODE)
                .responseMessage(CustomerUtils.DEPOSIT_SUCCESS_MESSAGE + user.getBalance())
                .build();
    }

    @Override
    public ResponseDto withdraw(DepositAndWithdrawDto depositAndWithdrawDto) {
        Customer user = customerRepository.existsByAccountNumber(depositAndWithdrawDto.getAccountNumber());
        if (user == null) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE_MESSAGE)
                    .build();
        }
        if (!user.getStatus().equals(CustomerUtils.LOGGEDIN)) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.AUTHORIZATION_ERROR_CODE)
                    .responseMessage(CustomerUtils.AUTHORIZATION_ERROR_MESSAGE)
                    .build();
        }
        if (user.getBalance().compareTo(depositAndWithdrawDto.getAmount()) < 0 || user.getBalance().compareTo(depositAndWithdrawDto.getAmount()) == 0) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.INVALID_AMOUNT_CODE)
                    .responseMessage(CustomerUtils.INSUFFICIENT_BAL_MESSAGE)
                    .build();
        }


        user.setBalance(user.getBalance().subtract(depositAndWithdrawDto.getAmount()));
        customerRepository.save(user);
        emailService.sendEmailAlert(EmailDto.builder()
                .subject(CustomerUtils.DEBIT + " ALERT!")
                .recipient(user.getEmail())
                .message("TRANSACTION RECEIPT BELOW: \n" +
                        TransactionDto.builder()
                                .senderName(user.getFirstName() + " " + user.getLastName())
                                .senderAccount(Year.now() + "XXXXXX")
                                .amount(depositAndWithdrawDto.getAmount())
                                .type(CustomerUtils.DEBIT)
                                .build().toString())
                .build());
        return ResponseDto.builder()
                .responseCode(CustomerUtils.SUCCESS_CODE)
                .responseMessage(CustomerUtils.WITHDRAW_SUCCESS_MESSAGE)
                .build();
    }

    @Override
    public ResponseDto transfer(TransferDto transferDto) {
        Customer sender = customerRepository.existsByAccountNumber(transferDto.getSenderAccount());
        Customer beneficiary = customerRepository.existsByAccountNumber(transferDto.getBeneficiaryAccount());

        if (sender == null || beneficiary == null) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(CustomerUtils.ACCOUNT_DOES_NOT_EXISTS_CODE_MESSAGE)
                    .build();
        }
        if (!sender.getStatus().equals(CustomerUtils.LOGGEDIN)) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.AUTHORIZATION_ERROR_CODE)
                    .responseMessage(CustomerUtils.AUTHORIZATION_ERROR_MESSAGE)
                    .build();
        }
        if (transferDto.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.INVALID_AMOUNT_CODE)
                    .responseMessage(CustomerUtils.INVALID_AMOUNT_MESSAGE)
                    .build();
        }
        if (sender.getBalance().compareTo(transferDto.getAmount()) < 0 || sender.getBalance().compareTo(transferDto.getAmount()) == 0) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.INVALID_AMOUNT_CODE)
                    .responseMessage(CustomerUtils.INSUFFICIENT_BAL_MESSAGE)
                    .build();
        }
        try {
            if (!Objects.equals(sender.getPassword(), new CustomerUtils().hashingInput(transferDto.getPassword()))) {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.AUTHORIZATION_ERROR_CODE)
                        .responseMessage(CustomerUtils.INCORRECT_PASSWORD_MESSAGE)
                        .build();
            }
        } catch (Exception ex) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.EXCEPTION_CODE)
                    .responseMessage(CustomerUtils.EXCEPTION_MESSAGE + ex)
                    .build();
        }

        try {

//        implement transaction model
            String ref = new CustomerUtils().generateTransactionReference();
            Transaction savedTransaction = saveTransaction(TransactionDto.builder()
                    .ref(ref)
                    .senderAccount(sender.getAccountNumber())
                    .senderName(sender.getFirstName() + " " + sender.getLastName())
                    .amount(transferDto.getAmount())
                    .beneficiaryAccount(beneficiary.getAccountNumber())
                    .beneficiaryName(beneficiary.getFirstName() + " " + beneficiary.getLastName())
                    .narration(transferDto.getTransNarration())
                    .status(CustomerUtils.SUCCESS)
                    .type(CustomerUtils.TRANSFER)
                    .build());

            Transaction transaction = transactionRepository.findByRef(ref);
            sender.setBalance(sender.getBalance().subtract(transferDto.getAmount()));
            beneficiary.setBalance(beneficiary.getBalance().add(transferDto.getAmount()));
            customerRepository.save(sender);
            customerRepository.save(beneficiary);
            String senderAccount = transaction.getSenderAccount();
            char c3 = senderAccount.charAt(3);
            char c4 = senderAccount.charAt(4);
            char c5 = senderAccount.charAt(5);
            char c6 = senderAccount.charAt(6);

            senderAccount = senderAccount.replace(c3, 'X');
            senderAccount = senderAccount.replace(c4, 'X');
            senderAccount = senderAccount.replace(c5, 'X');
            senderAccount = senderAccount.replace(c6, 'X');
            if (savedTransaction != null) {

                String[] datetime = transaction.getDate().toString().split("T");
                String date = datetime[0];
                String[] time = datetime[1].split("\\.");



                String senderMessage = "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "    body { font-family: Arial, sans-serif; }" +
                        "    .email-container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4; border: 1px solid #ddd; }" +
                        "    .email-header { background-color: #007BFF; color: white; padding: 10px; text-align: center; }" +
                        "    .email-body { padding: 20px; background-color: #fff; }" +
                        "    .email-body h3 { color: #333; }" +
                        "    .transaction-details { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                        "    .transaction-details th, .transaction-details td { border: 1px solid #ddd; padding: 8px; text-align: left; }" +
                        "    .transaction-details th { background-color: #f2f2f2; }" +
                        "    .email-footer { text-align: center; font-size: 12px; color: #666; margin-top: 20px; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='email-container'>" +
                        "    <div class='email-header'>" +
                        "        <h2>EBank Transaction Alert</h2>" +
                        "    </div>" +
                        "    <div class='email-body'>" +
                        "        <h3>Dear " + transaction.getSenderName() + ",</h3>" +
                        "        <p>Below are the details of your recent transaction:</p>" +
                        "        <table class='transaction-details'>" +
                        "            <tr>" +
                        "                <th>Date/Time: </th>" +
                        "                <td>" + date + " " + time[0] + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Reference No:</th>" +
                        "                <td>" + ref + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>From(You):</th>" +
                        "                <td>" + senderAccount + " - " + transaction.getSenderName() + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>To:</th>" +
                        "                <td>" + transaction.getBeneficiaryAccount() + " - " + transaction.getBeneficiaryName() + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Amount:</th>" +
                        "                <td>₦" + String.format("%,.2f", transaction.getAmount()) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Balance:</th>" +
                        "                <td>₦" + String.format("%,.2f", sender.getBalance()) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Narration:</th>" +
                        "                <td>" + transaction.getNarration() + "</td>" +
                        "            </tr>" +
                        "        </table>" +
                        "        <p>Thank you for banking with us!</p>" +
                        "    </div>" +
                        "    <div class='email-footer'>" +
                        "        <p>If you have any questions, please contact customer support.</p>" +
                        "    </div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";


                String beneficiaryMessage = "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "    body { font-family: Arial, sans-serif; }" +
                        "    .email-container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4; border: 1px solid #ddd; }" +
                        "    .email-header { background-color: #007BFF; color: white; padding: 10px; text-align: center; }" +
                        "    .email-body { padding: 20px; background-color: #fff; }" +
                        "    .email-body h3 { color: #333; }" +
                        "    .transaction-details { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                        "    .transaction-details th, .transaction-details td { border: 1px solid #ddd; padding: 8px; text-align: left; }" +
                        "    .transaction-details th { background-color: #f2f2f2; }" +
                        "    .email-footer { text-align: center; font-size: 12px; color: #666; margin-top: 20px; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='email-container'>" +
                        "    <div class='email-header'>" +
                        "        <h2>EBank Transaction Alert</h2>" +
                        "    </div>" +
                        "    <div class='email-body'>" +
                        "        <h3>Dear " + transaction.getSenderName() + ",</h3>" +
                        "        <p>Below are the details of the transaction:</p>" +
                        "        <table class='transaction-details'>" +
                        "            <tr>" +
                        "                <th>Date/Time: </th>" +
                        "                <td>" + date + " " + time[0] + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Reference No:</th>" +
                        "                <td>" + ref + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>From:</th>" +
                        "                <td>" + senderAccount + " - " + transaction.getSenderName() + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>To(You):</th>" +
                        "                <td>" + transaction.getBeneficiaryAccount() + " - " + transaction.getBeneficiaryName() + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Amount:</th>" +
                        "                <td>₦" + String.format("%,.2f", transaction.getAmount()) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Balance:</th>" +
                        "                <td>₦" + String.format("%,.2f", beneficiary.getBalance()) + "</td>" +
                        "            </tr>" +
                        "            <tr>" +
                        "                <th>Narration:</th>" +
                        "                <td>" + transaction.getNarration() + "</td>" +
                        "            </tr>" +
                        "        </table>" +
                        "        <p>Thank you for banking with us!</p>" +
                        "    </div>" +
                        "    <div class='email-footer'>" +
                        "        <p>If you have any questions, please contact customer support.</p>" +
                        "    </div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
                emailService.sendHtmlEmailAlert(EmailDto.builder()
                        .subject(CustomerUtils.DEBIT + " ALERT!")
                        .recipient(sender.getEmail())
                        .message(senderMessage)
                        .build());
                emailService.sendHtmlEmailAlert(EmailDto.builder()
                        .subject(CustomerUtils.CREDIT + " ALERT!")
                        .recipient(beneficiary.getEmail())
                        .message(beneficiaryMessage)
                        .build());


                return ResponseDto.builder()
                        .responseCode(CustomerUtils.SUCCESS_CODE)
                        .responseMessage(CustomerUtils.TRANSACTION_SUCCESSFUL)
                        .info(savedTransaction)
                        .build();
            } else {
                return ResponseDto.builder()
                        .responseCode(CustomerUtils.FAILED)
                        .responseMessage(CustomerUtils.TRANSACTION_ERROR_MESSAGE)
                        .info(transaction)
                        .build();
            }

        } catch (Exception ex) {
            return ResponseDto.builder()
                    .responseCode(CustomerUtils.EXCEPTION_CODE)
                    .responseMessage(CustomerUtils.EXCEPTION_MESSAGE + ex)
                    .build();
        }
    }

    @Override
    public Transaction saveTransaction(TransactionDto transactionDto) {

        try {
            Transaction transaction = new Transaction();
            transaction.setRef(transactionDto.getRef());
            transaction.setSenderAccount(transactionDto.getSenderAccount());
            transaction.setSenderName(transactionDto.getSenderName());
            transaction.setAmount(transactionDto.getAmount());
            transaction.setBeneficiaryAccount(transactionDto.getBeneficiaryAccount());
            transaction.setBeneficiaryName(transactionDto.getBeneficiaryName());
            transaction.setNarration(transactionDto.getNarration());
            transaction.setStatus(transactionDto.getStatus());
            transaction.setType(transactionDto.getType());
            transactionRepository.save(transaction);


            return transaction;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Customer> customers() {

        return customerRepository.findAll();
    }
}
