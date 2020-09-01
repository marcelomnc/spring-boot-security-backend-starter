package com.woundeddragons.securitystarter.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woundeddragons.securitystarter.business.model.CustomUserDetails;
import com.woundeddragons.securitystarter.business.service.CustomUserDetailsService;
import com.woundeddragons.securitystarter.web.api.Constants;
import com.woundeddragons.securitystarter.web.api.v1.response.JWTProcessorResponse;
import com.woundeddragons.securitystarter.web.common.JWTUtils;
import com.woundeddragons.securitystarter.web.common.WebSecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
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
import javax.servlet.ServletContext;
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
    private ServletContext servletContext;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletContextPath = this.servletContext.getContextPath();
        return !request.getRequestURI().startsWith(servletContextPath + Constants.API_PATH);
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

    private UsernamePasswordAuthenticationToken buildAuthenticationFromJWT(String jwt) {
        Claims claims = JWTUtils.parseClaims(jwt);
        // Extract the Username
        String username = claims.getSubject();
        CustomUserDetails customUserDetails = (CustomUserDetails) this.customUserDetailsService.loadUserByUsername(username);
        //TODO: Logica para validar usuario deshabilitado, eliminado, etc
        //throw XXXException dependiendo del caso

        List<String> securityRolesList = JWTUtils.getSecurityRoles(claims);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (securityRolesList != null && !securityRolesList.isEmpty()) {
            securityRolesList.stream().forEach(roleName -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
                logger.debug("Added Granted Authority: " + roleName);
            });
        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, grantedAuthorities);
    }
}