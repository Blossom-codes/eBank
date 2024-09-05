package com.blossom.eBank.service.serviceImpl;

import com.blossom.eBank.dto.EmailDto;
import com.blossom.eBank.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;




    public void sendHtmlEmailAlert(EmailDto emailDto) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(emailDto.getRecipient());
            helper.setSubject(emailDto.getSubject());
            helper.setText(emailDto.getMessage(), true);  // `true` enables HTML

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();  // Handle the exception accordingly
        }
    }
    @Override
    public void sendEmailAlert(EmailDto emailDto) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDto.getRecipient());
            simpleMailMessage.setText(emailDto.getMessage());
            simpleMailMessage.setSubject(emailDto.getSubject());

            javaMailSender.send(simpleMailMessage);
            System.out.println("Mail sent successfully");
        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}
