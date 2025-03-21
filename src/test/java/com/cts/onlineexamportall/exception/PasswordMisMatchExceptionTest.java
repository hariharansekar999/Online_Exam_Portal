package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class PasswordMisMatchExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Testing the default constructor
        PasswordMisMatchException exception = new PasswordMisMatchException();

        assertNotNull(exception); // Ensure the exception is not null
        assertNull(exception.getMessage()); // Default message should be null
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor
        String errorMessage = "Passwords do not match";
        PasswordMisMatchException exception = new PasswordMisMatchException(errorMessage);

        assertNotNull(exception); // Ensure the exception is not null
        assertEquals(errorMessage, exception.getMessage()); // The error message should match
    }
}
