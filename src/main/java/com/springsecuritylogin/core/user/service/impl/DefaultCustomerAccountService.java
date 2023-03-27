package com.springsecuritylogin.core.user.service.impl;


import com.springsecuritylogin.core.email.context.ForgotPasswordEmailContext;
import com.springsecuritylogin.core.email.service.EmailService;
import com.springsecuritylogin.core.exception.InvalidTokenException;
import com.springsecuritylogin.core.exception.UnkownIdentifierException;
import com.springsecuritylogin.core.security.jpa.SecureToken;
import com.springsecuritylogin.core.security.token.SecureTokenService;
import com.springsecuritylogin.core.security.token.repository.SecureTokenRepository;
import com.springsecuritylogin.core.user.jpa.data.UserEntity;
import com.springsecuritylogin.core.user.jpa.repository.UserRepository;
import com.springsecuritylogin.core.user.service.CustomerAccountService;
import com.springsecuritylogin.core.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Objects;


@Service("customerAccountService")
public class DefaultCustomerAccountService implements CustomerAccountService {

    @Resource
    UserService userService;

    @Resource
    private SecureTokenService secureTokenService;

    @Resource
    SecureTokenRepository secureTokenRepository;

    @Value("${site.base.url.https}")
    private String baseURL;

    @Resource
    private EmailService emailService;

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public void forgottenPassword(String userName) throws UnkownIdentifierException {
        UserEntity user= userService.getUserById(userName);
        sendResetPasswordEmail(user);
    }

    @Override
    public void updatePassword(String password, String token) throws InvalidTokenException, UnkownIdentifierException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        UserEntity user = userRepository.getOne(secureToken.getUser().getId());
        if(Objects.isNull(user)){
            throw new UnkownIdentifierException("unable to find user for the token");
        }
        secureTokenService.removeToken(secureToken);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }


    protected void sendResetPasswordEmail(UserEntity user) {
        SecureToken secureToken= secureTokenService.createSecureToken();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean loginDisabled(String username) {
        UserEntity user = userRepository.findByEmail(username);
        return user!=null ? user.isLoginDisabled() : false;
    }
}
