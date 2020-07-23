package com.woundeddragons.securitystarter.web.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woundeddragons.securitystarter.business.security.model.CustomUserDetails;
import com.woundeddragons.securitystarter.business.security.service.CustomUserDetailsService;
import com.woundeddragons.securitystarter.business.security.service.RoleByUserService;
import com.woundeddragons.securitystarter.web.security.api.Constants;
import com.woundeddragons.securitystarter.web.security.api.v1.response.JWTProcessorResponse;
import com.woundeddragons.securitystarter.web.security.common.WebSecurityConstants;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JWTProcessorFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RoleByUserService roleByUserService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().startsWith(Constants.API_PATH);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws ServletException, IOException {
        String header = req.getHeader(WebSecurityConstants.HEADER_STRING);
        if (header == null || !header.startsWith(WebSecurityConstants.JWT_PREFIX)) {
            fc.doFilter(req, res);
            return;
        }

        String jwt = header.substring(WebSecurityConstants.JWT_PREFIX.length());
        try {
            SecurityContextHolder.getContext().setAuthentication(buildAuthenticationFromJWT(jwt));
            fc.doFilter(req, res);
        } catch (UsernameNotFoundException | ExpiredJwtException | MalformedJwtException | SignatureException e) {
            int httpStatusCode = HttpStatus.UNAUTHORIZED.value();
            //TODO: Error codes
            int responseErrorCode = -1;
            String responseErrorMessage = null;
            if (e instanceof UsernameNotFoundException) {
                httpStatusCode = HttpStatus.NOT_FOUND.value();
                responseErrorCode = httpStatusCode;
                responseErrorMessage = "Username not found !";
            } else if (e instanceof ExpiredJwtException) {
                responseErrorCode = 99;
                responseErrorMessage = "Token is expired !";
            } else if (e instanceof MalformedJwtException) {
                responseErrorCode = 99;
                responseErrorMessage = "Token is malformed !";
            } else {
                responseErrorCode = 99;
                responseErrorMessage = "Token signature cannot be validated, token may be tampered !";
            }

            ObjectMapper jacksonObjectMapper = new ObjectMapper();
            JWTProcessorResponse toRet = new JWTProcessorResponse();
            toRet.addResponseError(responseErrorCode, responseErrorMessage);
            res.setStatus(httpStatusCode);
            res.setContentType("application/json");
            res.getOutputStream().write(jacksonObjectMapper.writeValueAsBytes(toRet));
        }
    }

    private UsernamePasswordAuthenticationToken buildAuthenticationFromJWT(String jwt) throws ExpiredJwtException {
        Claims claims = Jwts.parser()
                .setSigningKey(WebSecurityConstants.JWT_SECRET)
                .parseClaimsJws(jwt)
                .getBody();

        // Extract the Username
        String username = claims.getSubject();
        CustomUserDetails customUserDetails = (CustomUserDetails) this.customUserDetailsService.loadUserByUsername(username);
        //TODO: Logica para validar usuario deshabilitado, eliminado, etc
        //throw XXXException dependiendo del caso

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        List<String> rolesList = (List<String>) claims.get(WebSecurityConstants.JWT_ROLES_CLAIM);
        if (rolesList != null && !rolesList.isEmpty()) {
            rolesList.stream().forEach(roleName -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
                logger.debug("Added Granted Authority: " + roleName);
            });
        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, grantedAuthorities);
    }
}
