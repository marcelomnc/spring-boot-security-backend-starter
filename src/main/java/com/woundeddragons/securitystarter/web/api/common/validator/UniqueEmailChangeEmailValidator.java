package com.woundeddragons.securitystarter.web.api.v1.validator;

import com.woundeddragons.securitystarter.business.model.CustomUserDetails;
import com.woundeddragons.securitystarter.business.service.UserService;
import com.woundeddragons.securitystarter.web.api.v1.request.ChangeEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailChangeEmailValidator implements ConstraintValidator<UniqueEmailChangeEmail, ChangeEmailRequest> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(ChangeEmailRequest changeEmailRequest, ConstraintValidatorContext constraintValidatorContext) {
        //The only data that can be updated is the data related to the logged user
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return !this.userService.existsByEmail(changeEmailRequest.getEmail(), customUserDetails.getUser().getNmId());
    }
}
