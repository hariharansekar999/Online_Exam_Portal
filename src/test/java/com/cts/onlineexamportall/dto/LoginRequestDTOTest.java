package com.cts.onlineexamportall.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class LoginRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Set up the Validator for testing constraints
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoginRequest() {
        // A valid LoginRequestDTO object
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUserName("validuser");
        loginRequest.setPassword("securepassword");

        // Perform validation
        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(loginRequest);

        assertTrue(violations.isEmpty()); // Should have no violations
    }

    @Test
    void testUserNameBlank() {
        // A LoginRequestDTO object with a blank username
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUserName("");
        loginRequest.setPassword("securepassword");

        // Perform validation
        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty()); // Should have violations
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("User name is required")));
    }

    @Test
    void testPasswordBlank() {
        // A LoginRequestDTO object with a blank password
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUserName("validuser");
        loginRequest.setPassword("");

        // Perform validation
        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty()); // Should have violations
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password is required")));
    }

    @Test
    void testPasswordTooShort() {
        // A LoginRequestDTO object with a password shorter than 6 characters
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUserName("validuser");
        loginRequest.setPassword("12345");

        // Perform validation
        Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty()); // Should have violations
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password should be at least 6 characters long")));
    }
}
