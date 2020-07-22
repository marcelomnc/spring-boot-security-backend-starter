package com.woundeddragons.securitystarter.web.security.api.v1.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse extends BaseResponse {
    private String jwt;
    private boolean mustVerify2FACode;
}
