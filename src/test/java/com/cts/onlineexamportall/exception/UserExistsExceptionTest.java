package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class UserExistsExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Testing the default constructor
        UserExistsException exception = new UserExistsException();

        assertNotNull(exception); // Ensure the exception is instantiated
        assertNull(exception.getMessage()); // Default constructor should not set a message
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor
        String errorMessage = "User already exists";
        UserExistsException exception = new UserExistsException(errorMessage);

        assertNotNull(exception); // Ensure the exception is instantiated
        assertEquals(errorMessage, exception.getMessage()); // Ensure the provided message is correctly retrieved
    }
}
