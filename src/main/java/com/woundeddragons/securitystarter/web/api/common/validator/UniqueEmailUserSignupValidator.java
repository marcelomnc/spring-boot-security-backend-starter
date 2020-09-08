package com.woundeddragons.securitystarter.web.api.v1.validator;

import com.woundeddragons.securitystarter.business.model.User;
import com.woundeddragons.securitystarter.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueEmailUserSignupValidator implements ConstraintValidator<UniqueEmailUserSignup, String> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> match = this.userService.findByEmail(email);
        return match.isEmpty();
    }
}
