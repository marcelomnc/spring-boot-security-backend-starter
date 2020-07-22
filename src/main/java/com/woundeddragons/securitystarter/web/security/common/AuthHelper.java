package com.woundeddragons.securitystarter.web.security.common;

import com.woundeddragons.securitystarter.business.security.model.RoleByUser;
import com.woundeddragons.securitystarter.business.security.model.User;
import com.woundeddragons.securitystarter.business.security.service.RoleByUserService;
import com.woundeddragons.securitystarter.business.security.service.UserService;
import com.woundeddragons.securitystarter.web.security.api.v1.response.AuthenticationResponse;
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
        //TODO: Logica para validar usuario deshabilitado, eliminado, etc
        //throw XXXException dependiendo del caso

        AuthenticationResponse toRet = null;
        Collection<RoleByUser> rolesByUser = null;

        if (loginProcessCompleted) {
            //Updating last login date for the user
            user.setDtLastLoginOn(new Date());
            this.userService.save(user);
            rolesByUser = this.roleByUserService.findByUserId(user.getNmId());
        }

        String jwt = JWTBuilder.buildJWT(user, rolesByUser, !loginProcessCompleted);

        toRet = new AuthenticationResponse();
        toRet.setJwt(jwt);
        toRet.setMustVerify2FACode(!loginProcessCompleted);

        return toRet;
    }
}
