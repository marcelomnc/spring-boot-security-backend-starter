package com.woundeddragons.securitystarter.web.api.v1.request;

import com.woundeddragons.securitystarter.web.api.v1.validator.UniqueEmailChangeEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangeEmailRequest {
    //TODO: Logic to update email must include a JWT renewal
    @NotEmpty(message = "You must send a email with this request.")
    @Email
    @Size(min = 4, max = 255)
    @UniqueEmailChangeEmail
    private String email;
}
