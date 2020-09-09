package com.woundeddragons.securitystarter.web.api.common.delegate;

import com.woundeddragons.securitystarter.business.model.CustomUserDetails;
import com.woundeddragons.securitystarter.business.service.CustomUserDetailsService;
import com.woundeddragons.securitystarter.web.api.common.JWTUtils;
import com.woundeddragons.securitystarter.web.api.common.response.ErrorsEnum;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JWTProcessorDelegate {
    private static final Logger logger = LoggerFactory.getLogger(JWTProcessorDelegate.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public UsernamePasswordAuthenticationToken buildAuthenticationFromJWT(String jwt, boolean includeSecurityRolesClaim) {
        Claims claims = JWTUtils.parseClaims(jwt);
        String username = claims.getSubject();
        CustomUserDetails customUserDetails = (CustomUserDetails) this.customUserDetailsService.loadUserByUsername(username);

        if (!customUserDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(ErrorsEnum.CREDENTIALS_EXPIRED.getMessage());
        }

        if (!customUserDetails.isAccountNonLocked()) {
            throw new LockedException(ErrorsEnum.ACCOUNT_LOCKED.getMessage());
        }

        if (!customUserDetails.isEnabled()) {
            throw new DisabledException(ErrorsEnum.ACCOUNT_DISABLED.getMessage());
        }

        if (!customUserDetails.isAccountNonExpired()) {
            throw new AccountExpiredException(ErrorsEnum.ACCOUNT_EXPIRED.getMessage());
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (includeSecurityRolesClaim) {
            List<String> securityRolesList = JWTUtils.getSecurityRoles(claims);
            if (securityRolesList != null && !securityRolesList.isEmpty()) {
                securityRolesList.forEach(roleName -> {
                    grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
                    logger.debug("Added Granted Authority: " + roleName);
                });
            }
        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, grantedAuthorities);
    }
}
