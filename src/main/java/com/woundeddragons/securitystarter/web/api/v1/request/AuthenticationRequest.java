package com.woundeddragons.securitystarter.web.api.v1.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class AuthenticationRequest implements Serializable {
    @NotEmpty(message = "You must send a username with this request.")
    @Size(min = 5, max = 255)
    private String username;

    @NotEmpty(message = "You must send a password with this request.")
    @Size(min = 1, max = 255)
    private String password;
}