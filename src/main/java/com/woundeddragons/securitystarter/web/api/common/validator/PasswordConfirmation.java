package com.woundeddragons.securitystarter.web.api.v1.validator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConfirmationValidator.class)
public @interface PasswordConfirmation {
    String message() default "The password and its confirmation do not match.";

    Class[] groups() default {};

    Class[] payload() default {};
}
