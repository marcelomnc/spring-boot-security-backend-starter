package com.woundeddragons.securitystarter.web.common;

public class WebSecurityConstants {
    public static final String JWT_SECRET = "SecretKeyToGenJWTs";
    public static final String JWT_PREFIX = "Bearer ";
    public static final String JWT_ROLES_CLAIM = "roles";
    public static final String HEADER_STRING = "Authorization";
    public static final long JWT_EXPIRATION_MILLIS = 864000000; // 10 days
}
