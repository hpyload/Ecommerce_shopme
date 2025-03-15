package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncode = new BCryptPasswordEncoder();
        String rawPassword = "password";
        String passwordEncoded = passwordEncode.encode(rawPassword);
        System.out.println(passwordEncoded);
        boolean matches = passwordEncode.matches(rawPassword, passwordEncoded);
        assertThat(matches).isTrue();
    }
}
