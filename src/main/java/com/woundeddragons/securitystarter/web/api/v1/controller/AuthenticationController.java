package com.woundeddragons.securitystarter.web.api.v1.controller;

import com.woundeddragons.securitystarter.business.model.CustomUserDetails;
import com.woundeddragons.securitystarter.web.api.v1.Constants;
import com.woundeddragons.securitystarter.web.api.v1.request.AuthenticationRequest;
import com.woundeddragons.securitystarter.web.api.v1.response.AuthenticationResponse;
import com.woundeddragons.securitystarter.web.common.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public ResponseEntity doAuthentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        ResponseEntity toRet;

        try {
            Authentication authentication =
                    this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            logger.debug("After authentication: " + authentication + ", name: " + authentication.getName() + ", principal: " + authentication.getPrincipal() + ", credentials: " + authentication.getCredentials());

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            AuthenticationResponse authenticationResponse = this.authHelper.doAuthentication(customUserDetails.getUser(), !customUserDetails.getUser().isYn2faEnabled());

            toRet = ResponseEntity.ok().body(authenticationResponse);
        } catch (AuthenticationException e) {
            //TODO: Validar las posibles razones del porque no autentico e informar al frontend (Error codes)
            //Codigos especificos para cada caso:
            //USername no existe
            //PAssword no es
            //Usuario deshabilitado
            int responseErrorCode;
            String responseErrorMessage;
            if (e instanceof UsernameNotFoundException) {
                responseErrorCode = 99;
                responseErrorMessage = e.getMessage();
            } else if (e instanceof BadCredentialsException) {
                responseErrorCode = 99;
                responseErrorMessage = e.getMessage();
            } else {
                responseErrorCode = 99;
                responseErrorMessage = e.getMessage();
            }

            logger.debug("Authentication for username '" + authenticationRequest.getUsername() + "' failed.", e);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.addResponseError(responseErrorCode, responseErrorMessage);
            toRet = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }

        return toRet;
    }
}
