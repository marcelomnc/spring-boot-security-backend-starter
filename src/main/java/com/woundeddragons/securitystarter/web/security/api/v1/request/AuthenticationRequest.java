package com.woundeddragons.securitystarter.web.security.api.v1.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class AuthenticationRequest implements Serializable {
    @NotEmpty(message = "You must send a username with this request.")
    private String username;

    @NotEmpty(message = "You must send a password with this request.")
    private String password;
}
