package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidCredentialsException;
import com.well_sync.objects.UserCredentials;

/**
 * Manages the login and registration of users.
 */
public interface IUserAuthenticationHandler {
    UserCredentials.Role login(UserCredentials inputCreds) throws InvalidCredentialsException;

    void register(UserCredentials credentials) throws InvalidCredentialsException;
}
