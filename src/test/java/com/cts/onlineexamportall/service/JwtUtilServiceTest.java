package com.cts.onlineexamportall.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtilServiceTest {

    @InjectMocks
    private JwtUtilService jwtUtilService;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Generate a secure key for testing
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Set up JwtUtilService with the generated secret key
        jwtUtilService = new JwtUtilService() {
            {
                secret_key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            }
        };
    }

    @Test
    void testGenerateToken() {
        UUID userId = UUID.randomUUID();
        String username = "johndoe";

        String token = jwtUtilService.generateToken(userId, username);

        assertNotNull(token);
        assertTrue(token.length() > 0);

        // Extract claims from the token
        String extractedUsername = jwtUtilService.extractUserName(token);
        String extractedUserId = jwtUtilService.extractUserId(token);

        assertEquals(username, extractedUsername);
        assertEquals(userId.toString(), extractedUserId);
    }

    @Test
    void testExtractUserName() {
        UUID userId = UUID.randomUUID();
        String username = "johndoe";

        String token = jwtUtilService.generateToken(userId, username);

        String extractedUsername = jwtUtilService.extractUserName(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractUserId() {
        UUID userId = UUID.randomUUID();
        String username = "johndoe";

        String token = jwtUtilService.generateToken(userId, username);

        String extractedUserId = jwtUtilService.extractUserId(token);

        assertEquals(userId.toString(), extractedUserId);
    }

    @Test
    void testValidateToken_Valid() {
        UUID userId = UUID.randomUUID();
        String username = "johndoe";

        String token = jwtUtilService.generateToken(userId, username);

        UserDetails mockUserDetails = User.builder().username(username).password("password").roles("USER").build();

        boolean isValid = jwtUtilService.validateToken(token, mockUserDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_Invalid() {
        UUID userId = UUID.randomUUID();
        String username = "johndoe";

        String token = jwtUtilService.generateToken(userId, username);

        // Create UserDetails with a different username
        UserDetails mockUserDetails = User.builder().username("wronguser").password("password").roles("USER").build();

        boolean isValid = jwtUtilService.validateToken(token, mockUserDetails);

        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        UUID userId = UUID.randomUUID();
        String username = "johndoe";

        // Generate token with a future expiration time
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Valid for 1 hour
                .signWith(jwtUtilService.getKey())
                .compact();

        boolean isExpired = jwtUtilService.validateToken(token, 
            User.builder().username(username).password("password").roles("USER").build());

        assertTrue(!isExpired, "The token should not be expired.");
    }
}