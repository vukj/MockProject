package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.EmailTemplate;

import javax.mail.MessagingException;

public interface EmailSenderService {

    void sendEmail(EmailTemplate emailTemplate) throws MessagingException;
}
