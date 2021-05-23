package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.EmailTemplate;
import com.fa.training.mockproject.services.EmailSenderService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Service
@Transactional
public class EmailSenderServiceImpl implements EmailSenderService {
    private JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Use JavaMailSender to send email
     *
     * @param emailTemplate
     * @throws MessagingException
     */
    @Async
    @Override
    public void sendEmail(EmailTemplate emailTemplate) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(emailTemplate.getFromAddress());
        helper.setTo(emailTemplate.getToAddress());
        helper.setSubject(emailTemplate.getSubject());
        helper.setText(emailTemplate.getContent(), true);
        javaMailSender.send(message);
    }
}

