package com.woundeddragons.securitystarter.web.api.v1.request;

import com.woundeddragons.securitystarter.web.api.v1.validator.UniqueEmailUserSignup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDataEmailRequest extends UserDataRequest {
    @NotEmpty(message = "You must send a email with this request.")
    @Email
    @Size(min = 4, max = 255)
    @UniqueEmailUserSignup
    private String email;
}
