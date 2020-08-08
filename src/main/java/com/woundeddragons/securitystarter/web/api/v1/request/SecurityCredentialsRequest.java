package com.woundeddragons.securitystarter.web.api.v1.request;

public interface SecurityCredentialsRequest {
    String getPassword();

    String getPasswordConfirmation();
}