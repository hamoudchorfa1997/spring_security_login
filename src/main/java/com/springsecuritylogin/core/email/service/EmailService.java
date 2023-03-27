package com.springsecuritylogin.core.email.service;

import com.springsecuritylogin.core.email.context.AbstractEmailContext;

import javax.mail.MessagingException;

public interface EmailService {

    void sendMail(final AbstractEmailContext email) throws MessagingException;
}
