package com.woundeddragons.securitystarter.web.api.common;

import com.woundeddragons.securitystarter.business.model.RoleByUser;
import com.woundeddragons.securitystarter.business.model.User;
import com.woundeddragons.securitystarter.business.service.RoleByUserService;
import com.woundeddragons.securitystarter.business.service.UserService;
import com.woundeddragons.securitystarter.web.api.v1.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
public class AuthHelper {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleByUserService roleByUserService;

    public AuthenticationResponse doAuthentication(User user, boolean loginProcessCompleted) {
        Collection<RoleByUser> rolesByUser = null;
        if (loginProcessCompleted) {
            //Updating last login date for the user
            user.setDtLastLoginOn(new Date());
            this.userService.save(user);
            rolesByUser = this.roleByUserService.findByUserId(user.getNmId());
        }

        String jwt = JWTUtils.buildJWT(user, rolesByUser, !loginProcessCompleted);

        AuthenticationResponse toRet = new AuthenticationResponse();
        toRet.setJwt(jwt);
        toRet.setMustVerify2FACode(!loginProcessCompleted);
        return toRet;
    }
}