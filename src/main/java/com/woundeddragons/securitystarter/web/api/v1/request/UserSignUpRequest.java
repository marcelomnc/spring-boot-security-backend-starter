package com.woundeddragons.securitystarter.web.api.v1.request;

import com.woundeddragons.securitystarter.web.api.common.validator.PasswordConfirmation;
import com.woundeddragons.securitystarter.web.api.common.validator.UniqueEmailUserSignup;
import com.woundeddragons.securitystarter.web.api.common.validator.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordConfirmation
public class UserSignUpRequest extends UserDataRequest implements SecurityCredentialsRequest {
    @NotNull(message = "You must send an email with this request.")
    @Email
    @Size(min = 4, max = 255)
    @UniqueEmailUserSignup
    private String email;
    @NotNull(message = "You must send a password with this request.")
    @ValidPassword
    private String password;
    @NotEmpty(message = "You must send a password confirmation with this request.")
    private String passwordConfirmation;
}
