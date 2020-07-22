package com.woundeddragons.securitystarter.web.security.api.v1.controller;

import com.woundeddragons.securitystarter.business.security.common.SecurityRole;
import com.woundeddragons.securitystarter.business.security.model.CustomUserDetails;
import com.woundeddragons.securitystarter.business.security.model.User;
import com.woundeddragons.securitystarter.business.security.service.UserService;
import com.woundeddragons.securitystarter.web.security.api.v1.Constants;
import com.woundeddragons.securitystarter.web.security.api.v1.request.ChangePasswordRequest;
import com.woundeddragons.securitystarter.web.security.api.v1.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
public class ChangePasswordController {
    public static final String PATH = "/user/changepassword";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PutMapping(path = PATH)
    public ResponseEntity doChangePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        ResponseEntity toRet = null;
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //The only data that can be updated is the data related to the logged user
        User toUpdate = this.userService.findById(customUserDetails.getUser().getNmId()).get();

        if (this.passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), toUpdate.getDsPassword())) {
            toUpdate.setDsPassword(this.passwordEncoder.encode(changePasswordRequest.getPassword()));
            this.userService.save(toUpdate);
            customUserDetails.getUser().setDsPassword(toUpdate.getDsPassword());
            toRet = ResponseEntity.ok().build();
        } else {
            BaseResponse baseResponse = new BaseResponse();
            //TODO: Error Codes and rules for changing passwords (not equal or whatever)
            baseResponse.addResponseError(99, "The current password does not match.");
            toRet = ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }

        return toRet;
    }
}
