package com.springsecuritylogin.core.user.service;

import com.springsecuritylogin.core.exception.InvalidTokenException;
import com.springsecuritylogin.core.exception.UnkownIdentifierException;

public interface CustomerAccountService {

    void forgottenPassword(final String userName) throws UnkownIdentifierException;
    void updatePassword(final String password, final String token) throws InvalidTokenException, UnkownIdentifierException;
    boolean loginDisabled(final String username);
}
