package com.woundeddragons.securitystarter.web.security.api.v1.request;

public interface SecurityCredentialsRequest {
    String getPassword();

    String getPasswordConfirmation();
}