package com.cts.onlineexamportall.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cts.onlineexamportall.enums.Role;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Initialize the Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserRequest() {
        // Create a valid UserRequest object
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("validuser");
        userRequest.setEmail("validuser@example.com");
        userRequest.setPassword("securepassword");
        userRequest.setRole(Role.STUDENT);

        // Perform validation
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertTrue(violations.isEmpty()); // Should be no violations
    }

    @Test
    void testUserNameBlank() {
        // Create a UserRequest object with a blank username
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("");
        userRequest.setEmail("validuser@example.com");
        userRequest.setPassword("securepassword");
        userRequest.setRole(Role.STUDENT);

        // Perform validation
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertFalse(violations.isEmpty()); // Should have violations
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("User name is required")));
    }

    @Test
    void testUserNameTooLong() {
        // Create a UserRequest object with a username exceeding 50 characters
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("a".repeat(51));
        userRequest.setEmail("validuser@example.com");
        userRequest.setPassword("securepassword");
        userRequest.setRole(Role.ADMIN);

        // Perform validation
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("User name cannot exceed 50 characters")));
    }

    @Test
    void testInvalidEmail() {
        // Create a UserRequest object with an invalid email
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("validuser");
        userRequest.setEmail("invalid-email");
        userRequest.setPassword("securepassword");
        userRequest.setRole(Role.EXAMINER);

        // Perform validation
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email should be valid")));
    }

    @Test
    void testPasswordTooShort() {
        // Create a UserRequest object with a password less than 6 characters
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("validuser");
        userRequest.setEmail("validuser@example.com");
        userRequest.setPassword("123");
        userRequest.setRole(Role.STUDENT);

        // Perform validation
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password should be at least 6 characters long")));
    }

    @Test
    void testPasswordBlank() {
        // Create a UserRequest object with a blank password
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("validuser");
        userRequest.setEmail("validuser@example.com");
        userRequest.setPassword("");
        userRequest.setRole(Role.ADMIN);

        // Perform validation
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password is required")));
    }
}
