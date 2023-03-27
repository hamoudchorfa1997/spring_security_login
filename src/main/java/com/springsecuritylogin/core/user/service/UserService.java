package com.springsecuritylogin.core.user.service;

import com.springsecuritylogin.core.exception.InvalidTokenException;
import com.springsecuritylogin.core.exception.UnkownIdentifierException;
import com.springsecuritylogin.core.exception.UserAlreadyExistException;
import com.springsecuritylogin.core.user.jpa.data.UserEntity;
import com.springsecuritylogin.web.data.user.MfaTokenData;
import com.springsecuritylogin.web.data.user.UserData;
import dev.samstevens.totp.exceptions.QrGenerationException;

public interface UserService {

    void register(final UserData user) throws UserAlreadyExistException;
    boolean checkIfUserExist(final String email);
    void sendRegistrationConfirmationEmail(final UserEntity user);
    boolean verifyUser(final String token) throws InvalidTokenException;
    UserEntity getUserById(final String id) throws UnkownIdentifierException;
    MfaTokenData mfaSetup(final String email) throws UnkownIdentifierException, QrGenerationException;
}
