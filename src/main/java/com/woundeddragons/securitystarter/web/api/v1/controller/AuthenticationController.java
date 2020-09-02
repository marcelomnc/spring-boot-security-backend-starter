package com.woundeddragons.securitystarter.web.api.v1.controller;

import com.woundeddragons.securitystarter.business.model.CustomUserDetails;
import com.woundeddragons.securitystarter.web.api.v1.Constants;
import com.woundeddragons.securitystarter.web.api.v1.request.AuthenticationRequest;
import com.woundeddragons.securitystarter.web.api.v1.response.AuthenticationResponse;
import com.woundeddragons.securitystarter.web.common.AuthHelper;
import com.woundeddragons.securitystarter.web.common.ErrorsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    public static final String PATH = "/auth";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthHelper authHelper;

    @PostMapping(path = PATH)
    public ResponseEntity<AuthenticationResponse> doAuthentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        ResponseEntity<AuthenticationResponse> toRet;

        try {
            final Authentication authentication =
                    this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            logger.debug("After authentication: " + authentication + ", name: " + authentication.getName() + ", principal: " + authentication.getPrincipal() + ", credentials: " + authentication.getCredentials());

            final CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            final AuthenticationResponse authenticationResponse = this.authHelper.doAuthentication(customUserDetails.getUser(), !customUserDetails.getUser().isYn2faEnabled());

            toRet = ResponseEntity.ok().body(authenticationResponse);
        } catch (AuthenticationException e) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            logger.debug("Authentication for username '" + authenticationRequest.getUsername() + "' failed.", e);

            ErrorsEnum errorsEnum = null;
            if (e instanceof UsernameNotFoundException) {
                errorsEnum = ErrorsEnum.USERNAME_NOT_FOUND;
            } else if (e instanceof BadCredentialsException) {
                errorsEnum = ErrorsEnum.BAD_CREDENTIALS;
            } else if (e instanceof CredentialsExpiredException) {
                errorsEnum = ErrorsEnum.CREDENTIALS_EXPIRED;
            } else if (e instanceof LockedException) {
                errorsEnum = ErrorsEnum.ACCOUNT_LOCKED;
            } else if (e instanceof DisabledException) {
                errorsEnum = ErrorsEnum.ACCOUNT_DISABLED;
            } else if (e instanceof AccountExpiredException) {
                errorsEnum = ErrorsEnum.ACCOUNT_EXPIRED;
            } else {
                errorsEnum = ErrorsEnum.GENERIC_AUTH;
            }

            authenticationResponse.addResponseError(errorsEnum);
            toRet = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }

        return toRet;
    }
}
