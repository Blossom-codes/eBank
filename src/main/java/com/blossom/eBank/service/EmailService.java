package com.blossom.eBank.service;

import com.blossom.eBank.dto.EmailDto;

public interface EmailService{
    void sendEmailAlert(EmailDto emailDto);
    void sendHtmlEmailAlert(EmailDto emailDto);
}
