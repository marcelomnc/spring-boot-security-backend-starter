package com.woundeddragons.securitystarter.web.security.common;

import com.woundeddragons.securitystarter.business.security.common.SecurityRole;
import com.woundeddragons.securitystarter.business.security.model.RoleByUser;
import com.woundeddragons.securitystarter.business.security.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class JWTBuilder {

    public static String buildJWT(User user, Collection<RoleByUser> rolesByUser, boolean set2FACodeVerificationRole) {
        Date tokenIssuedAtDate = new Date();
        Date tokenExpirationDate = new Date(tokenIssuedAtDate.getTime() + WebSecurityConstants.JWT_EXPIRATION_MILLIS);

        //TODO: Setear JTI o no ?
        Claims claims = Jwts.claims();
        claims.setSubject(user.getDsEmail())
                .setId("dewde32323")
                .setIssuedAt(tokenIssuedAtDate)
                .setExpiration(tokenExpirationDate);

        List<String> rolesList = new ArrayList<>();

        if (set2FACodeVerificationRole) {
            //Set only 2FA_CODE_VERIFICATION_ROLE
            rolesList.add(SecurityRole.ROLE_2FA_CODE_VERIFICATION.getName());
        } else {
            //Set all real user roles from db
            rolesByUser.stream().forEach(roleByUser -> {
                rolesList.add(roleByUser.getNmRoleId().getDsName());
            });
        }
        claims.put(WebSecurityConstants.JWT_ROLES_CLAIM, rolesList);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, WebSecurityConstants.JWT_SECRET)
                .compact();
    }
}
