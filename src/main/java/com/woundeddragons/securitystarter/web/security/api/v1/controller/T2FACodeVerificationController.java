package com.woundeddragons.securitystarter.web.security.api.v1.controller;

import com.woundeddragons.securitystarter.business.security.common.SecurityRole;
import com.woundeddragons.securitystarter.business.security.model.CustomUserDetails;
import com.woundeddragons.securitystarter.web.security.api.v1.Constants;
import com.woundeddragons.securitystarter.web.security.api.v1.request.T2FACodeVerificationRequest;
import com.woundeddragons.securitystarter.web.security.api.v1.response.AuthenticationResponse;
import com.woundeddragons.securitystarter.web.security.common.AuthHelper;
import org.jboss.aerogear.security.otp.Totp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_2FA_CODE_VERIFICATION})
public class T2FACodeVerificationController {
    private static final Logger logger = LoggerFactory.getLogger(T2FACodeVerificationController.class);
    public static final String PATH = "/2fa";

    @Autowired
    private AuthHelper authHelper;

    @PostMapping(path = PATH)
    public ResponseEntity doAuthentication(@RequestBody @Valid T2FACodeVerificationRequest t2FACodeVerificationRequest) {
        ResponseEntity toRet = null;
        AuthenticationResponse authenticationResponse = null;

        logger.debug("T2FACodeVerificationController verifying 2FA code '" + t2FACodeVerificationRequest.getT2FACode() + "'.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        //Verify 2fa
        Totp totp = new Totp(customUserDetails.getUser().getDs2faSecret());
        if (totp.verify(t2FACodeVerificationRequest.getT2FACode())) {
            authenticationResponse = this.authHelper.doAuthentication(customUserDetails.getUser(), true);
            toRet = ResponseEntity.ok().body(authenticationResponse);
        } else {
            authenticationResponse = new AuthenticationResponse();
            //TODO: Error code
            authenticationResponse.addResponseError(99, "The 2fa code: '" + t2FACodeVerificationRequest.getT2FACode() + "', is not valid !");
            authenticationResponse.setMustVerify2FACode(true);
            toRet = ResponseEntity.badRequest().body(authenticationResponse);
        }

        return toRet;
    }
}
