package com.woundeddragons.securitystarter.web.security.api.v1.request;

import com.woundeddragons.securitystarter.web.security.api.v1.validator.PasswordConfirmation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordConfirmation
public class ChangePasswordRequest implements SecurityCredentialsRequest {
    @NotEmpty(message = "You must send the current password with this request.")
    @Size(min = 10, max = 50)
    private String currentPassword;
    @NotEmpty(message = "You must send a new password with this request.")
    @Size(min = 10, max = 50)
    private String password;
    @NotEmpty(message = "You must send a new password confirmation with this request.")
    @Size(min = 10, max = 50)
    private String passwordConfirmation;
}
