package com.blossom.eBank.utils;

import com.blossom.eBank.dto.LoginDto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Year;
import java.util.Random;

public class CustomerUtils {
    public static final String SUCCESS_CODE = "200";
    public static final String LOGIN_SUCCESS_CODE = "021";
    public static final String BALANCE_ENQUIRY_SUCCESS = "022";
    public static final String NAME_ENQUIRY_SUCCESS = "023";
    public static final String TRANSFER_SUCCESS = "024";
    public static final String ACCOUNT_EXISTS_CODE = "300";
    public static final String AUTHORIZATION_ERROR_CODE = "400";
    public static final String EXCEPTION_CODE = "500";
    public static final String LOGIN_ERROR_CODE = "051";
    public static final String LOGOUT_ERROR_CODE = "052";
    public static final String ENQUIRY_ERROR_CODE = "053";
    public static final String ACCOUNT_DOES_NOT_EXISTS_CODE = "054";
    public static final String INVALID_AMOUNT_CODE = "055";
    public static final String TRANSFER_ERROR_CODE = "056";
    public static final String TRANSACTION_ERROR_CODE = "057";

    public static final String SUCCESS_MESSAGE = "User Registration was successful!, Login to activate your account.";
    public static final String DEPOSIT_SUCCESS_MESSAGE = "Deposit has been made successfully";
    public static final String WITHDRAW_SUCCESS_MESSAGE = "Withdrawal has been made successfully";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Transfer was successful";
    public static final String TRANSACTION_SUCCESSFUL = "Transaction was successful";
    public static final String BALANCE_ENQUIRY_MESSAGE = "Your account balance is ";
    public static final String NAME_ENQUIRY_MESSAGE = "Account name is ";
    public static final String ENQUIRY_ERROR_MESSAGE = "An error occurred, enter a valid enquiry ";
    public static final String LOGIN_WELCOME_MESSAGE = "Welcome!, Login was successful.";
    public static final String LOGIN_EXIT_MESSAGE = "Goodbye!";
    public static final String LOGIN_ERROR_MESSAGE = "Incorrect Email or Password, Try again.";
    public static final String TRANSACTION_ERROR_MESSAGE = "Transaction failed";
    public static final String EXCEPTION_MESSAGE = "Something went wrong! : ";
    public static final String AUTHORIZATION_ERROR_MESSAGE = "Invalid authorization, make sure you're logged in";
    public static final String INCORRECT_PASSWORD_MESSAGE = "Incorrect Password!";
    public static final String INCORRECT_PIN_MESSAGE = "Incorrect Pin!";
    public static final String INVALID_AMOUNT_MESSAGE = "Invalid Amount, Please enter a valid amount. ";
    public static final String INSUFFICIENT_BAL_MESSAGE = "Sorry, Insufficient Balance";
    public static final String ACCOUNT_EMAIL_EXISTS_MESSAGE = "User with that email already exists !";
    public static final String ACCOUNT_PHONE_NUMBER_EXISTS_MESSAGE = "User with that phone number already exists !";
    public static final String ACCOUNT_DOES_NOT_EXISTS_CODE_MESSAGE = "Invalid account number: Account not found!";
    public static final String LOGGEDIN = "ACTIVE";
    public static final String NOTLOGGEDIN = "INACTIVE";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

//    TRANSACTION TYPE
    public static final String DEBIT = "DEBIT";
    public static final String CREDIT = "CREDIT";
    public static final String TRANSFER = "TRANSFER";




    public String generateAccountNumber() {
        String prefix = String.valueOf(Year.now());
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        int num = random.nextInt(min, max);
        String suffix = Integer.toString(num);
        return prefix + suffix;
    }
 public String generateTransactionReference() {
        String prefix = "TRAN";
        int min = 100000; int min_plus = 000000;
        int max = 999999; int max_plus = 999999;
        Random random = new Random();
        int num = random.nextInt(min, max);
        int num_plus = random.nextInt(min_plus, max_plus);
        String suffix = Integer.toString(num)+Integer.toString(num_plus);
        return prefix + suffix;
    }

    public String hashingInput(String input) throws NoSuchAlgorithmException {
//        Encryption(Hashing)
        MessageDigest md = MessageDigest.getInstance("SHA-256");
//            encrypt and convert password to byte
        byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
//             convert hashed byte to hexadecimal format
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashedBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

}
