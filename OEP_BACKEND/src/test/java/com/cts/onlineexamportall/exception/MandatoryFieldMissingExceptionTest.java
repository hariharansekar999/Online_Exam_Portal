package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class MandatoryFieldMissingExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Testing the default constructor
        MandatoryFieldMissingException exception = new MandatoryFieldMissingException();

        assertNotNull(exception); // Ensure the exception is not null
        assertNull(exception.getMessage()); // Default message should be null
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor
        String errorMessage = "Mandatory field is missing";
        MandatoryFieldMissingException exception = new MandatoryFieldMissingException(errorMessage);

        assertNotNull(exception); // Ensure the exception is not null
        assertEquals(errorMessage, exception.getMessage()); // The error message should match
    }
}
