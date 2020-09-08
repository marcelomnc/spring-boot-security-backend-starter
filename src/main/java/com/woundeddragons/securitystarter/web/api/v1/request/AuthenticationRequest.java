package com.woundeddragons.securitystarter.web.api.v1.request;

import com.woundeddragons.securitystarter.web.api.common.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthenticationRequest extends BaseRequest {
    @NotNull(message = "You must send an username with this request.")
    @Size(min = 5, max = 255)
    private String username;

    @NotNull(message = "You must send a password with this request.")
    @Size(min = 1, max = 255)
    private String password;
}