package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class InvalidResponseExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Testing the default constructor
        InvalidResponseException exception = new InvalidResponseException();

        assertNotNull(exception);
        assertNull(exception.getMessage()); // No message should be set in the default constructor
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor
        String errorMessage = "Invalid response provided";
        InvalidResponseException exception = new InvalidResponseException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage()); // The error message should match
    }
}
