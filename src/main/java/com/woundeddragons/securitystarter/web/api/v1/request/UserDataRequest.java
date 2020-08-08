package com.woundeddragons.securitystarter.web.api.v1.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDataRequest {
    @NotEmpty(message = "You must send a first name with this request.")
    @Size(min = 1, max = 100)
    private String firstName;
    @NotEmpty(message = "You must send a last name with this request.")
    @Size(min = 1, max = 100)
    private String lastName;
    private boolean t2FAEnabled;
}
