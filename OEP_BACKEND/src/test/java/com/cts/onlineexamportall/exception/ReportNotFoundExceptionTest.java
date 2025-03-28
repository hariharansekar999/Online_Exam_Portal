package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ReportNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Testing the default constructor
        ReportNotFoundException exception = new ReportNotFoundException();

        assertNotNull(exception); // Ensure the exception object is not null
        assertNull(exception.getMessage()); // Message should be null for the default constructor
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor
        String errorMessage = "Report not found";
        ReportNotFoundException exception = new ReportNotFoundException(errorMessage);

        assertNotNull(exception); // Ensure the exception object is not null
        assertEquals(errorMessage, exception.getMessage()); // Verify the provided error message
    }
}
