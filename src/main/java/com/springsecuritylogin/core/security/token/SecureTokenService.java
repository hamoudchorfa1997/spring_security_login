package com.springsecuritylogin.core.security.token;

import com.springsecuritylogin.core.security.jpa.SecureToken;

public interface SecureTokenService {

    SecureToken createSecureToken();
    void saveSecureToken(final SecureToken token);
    SecureToken findByToken(final String token);
    void removeToken(final SecureToken token);
    void removeTokenByToken(final String token);
}
