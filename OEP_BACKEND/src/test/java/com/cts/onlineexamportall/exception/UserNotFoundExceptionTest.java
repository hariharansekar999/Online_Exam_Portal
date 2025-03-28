package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class UserNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Testing the default constructor
        UserNotFoundException exception = new UserNotFoundException();

        assertNotNull(exception); // Ensure the exception is instantiated
        assertNull(exception.getMessage()); // Default constructor should not set a message
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor
        String errorMessage = "User not found";
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        assertNotNull(exception); // Ensure the exception is instantiated
        assertEquals(errorMessage, exception.getMessage()); // Ensure the provided message is correctly retrieved
    }
}
