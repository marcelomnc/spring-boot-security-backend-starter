package com.woundeddragons.securitystarter.web.api.common.validator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailUserSignupValidator.class)
public @interface UniqueEmailUserSignup {
    String message() default "An account with the specified email already exists.";

    Class[] groups() default {};

    Class[] payload() default {};
}
