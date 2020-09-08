package com.woundeddragons.securitystarter.web.api.common.validator;

import com.woundeddragons.securitystarter.web.api.v1.request.SecurityCredentialsRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, SecurityCredentialsRequest> {
    @Override
    public boolean isValid(SecurityCredentialsRequest securityCredentialsRequest, ConstraintValidatorContext constraintValidatorContext) {
        String password = securityCredentialsRequest.getPassword();
        String passwordConfirmation = securityCredentialsRequest.getPasswordConfirmation();

        boolean toRet = false;
        if (password != null
                && passwordConfirmation != null
                && !"".equals(password)
                && !"".equals(passwordConfirmation)) {
            toRet = password.equals(passwordConfirmation);
        }

        return toRet;
    }
}