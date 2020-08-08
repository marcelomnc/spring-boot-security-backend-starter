package com.woundeddragons.securitystarter.web.api.v1.controller;

import com.woundeddragons.securitystarter.business.common.SecurityHelper;
import com.woundeddragons.securitystarter.business.model.User;
import com.woundeddragons.securitystarter.business.service.UserService;
import com.woundeddragons.securitystarter.web.api.v1.Constants;
import com.woundeddragons.securitystarter.web.api.v1.request.UserSignUpRequest;
import com.woundeddragons.securitystarter.web.api.v1.response.UserSignUpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.HttpURLConnection;
import java.util.Date;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
public class UserSignUpController {
    private static final Logger logger = LoggerFactory.getLogger(UserSignUpController.class);
    public static final String PATH = "/user/signup";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping(path = PATH)
    public ResponseEntity doUserSignUp(@RequestBody @Valid UserSignUpRequest userSignUpRequest) {
        ResponseEntity toRet = null;

        //TODO: Usar model mapper ?
        User toSignUp = new User();
        toSignUp.setDsEmail(userSignUpRequest.getEmail());
        toSignUp.setDsFirstName(userSignUpRequest.getFirstName());
        toSignUp.setDsLastName(userSignUpRequest.getLastName());
        toSignUp.setDsPassword(this.passwordEncoder.encode(userSignUpRequest.getPassword()));
        toSignUp.setYn2faEnabled(userSignUpRequest.isT2FAEnabled());
        toSignUp.setDtCreatedOn(new Date());

        try {
            if (userSignUpRequest.isT2FAEnabled()) {
                //Generate 2FA random secret
                toSignUp.setDs2faSecret(SecurityHelper.generateSecretKey());
            }
            User signedUp = this.userService.signUp(toSignUp);

            UserSignUpResponse userSignUpResponse = new UserSignUpResponse();
            if (userSignUpRequest.isT2FAEnabled()) {
                //Generate 2FA qr code image url
                userSignUpResponse.setT2FAQRCodeImageURL(SecurityHelper.generate2FAQRCodeImageURL(signedUp));
            }
            userSignUpResponse.setT2FAEnabled(signedUp.isYn2faEnabled());

            toRet = ResponseEntity.ok(userSignUpResponse);
        } catch (Exception e) {
            logger.debug("Cannot signup the user.", e);
            toRet = ResponseEntity.status(HttpURLConnection.HTTP_CONFLICT).build();
        }

        //TODO: Que devolvemos ?
        return toRet;
    }
}
