package com.woundeddragons.securitystarter.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

final class BCryptEncoderTool {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = "abcde$$12345";
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        System.out.println("Original password: '" + password + "'.");
        System.out.println("Hashed password: '" + hashedPassword + "'.");
    }
}
