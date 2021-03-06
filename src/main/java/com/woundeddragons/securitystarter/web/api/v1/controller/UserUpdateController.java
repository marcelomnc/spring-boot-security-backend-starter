package com.woundeddragons.securitystarter.web.api.v1.controller;

import com.woundeddragons.securitystarter.business.common.SecurityHelper;
import com.woundeddragons.securitystarter.business.common.SecurityRole;
import com.woundeddragons.securitystarter.business.model.CustomUserDetails;
import com.woundeddragons.securitystarter.business.model.User;
import com.woundeddragons.securitystarter.business.service.UserService;
import com.woundeddragons.securitystarter.web.api.v1.Constants;
import com.woundeddragons.securitystarter.web.api.v1.request.UserDataRequest;
import com.woundeddragons.securitystarter.web.api.v1.response.UserAccountDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
public class UserUpdateController {
    public static final String PATH = "/user";

    @Autowired
    private UserService userService;

    @PutMapping(path = PATH)
    public ResponseEntity<UserAccountDataResponse> doUpdate(@RequestBody @Valid UserDataRequest userDataRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //The only data that can be updated is the data related to the logged user
        User toUpdate = this.userService.findById(customUserDetails.getUser().getNmId()).get();
        toUpdate.setDsFirstName(userDataRequest.getFirstName());
        toUpdate.setDsLastName(userDataRequest.getLastName());

        UserAccountDataResponse response = new UserAccountDataResponse();
        if (userDataRequest.isT2FAEnabled() && !toUpdate.isYn2faEnabled()) {
            //User has activated 2FA
            //Generate 2FA random secret and qr code image url
            toUpdate.setDs2faSecret(SecurityHelper.generateSecretKey());
            response.setT2FAQRCodeImageURL(SecurityHelper.generate2FAQRCodeImageURL(toUpdate));
        } else if (!userDataRequest.isT2FAEnabled()) {
            //User has deactivated 2FA
            toUpdate.setDs2faSecret(null);
        }
        toUpdate.setYn2faEnabled(userDataRequest.isT2FAEnabled());
        response.setT2FAEnabled(toUpdate.isYn2faEnabled());
        this.userService.save(toUpdate);

        return ResponseEntity.ok(response);
    }
}
