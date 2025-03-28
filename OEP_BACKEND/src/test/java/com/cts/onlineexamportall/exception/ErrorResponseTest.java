package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ErrorResponseTest {

    @Test
    void testNoArgsConstructor() {
        // Testing the no-argument constructor
        ErrorResponse errorResponse = new ErrorResponse();

        assertNotNull(errorResponse);
        assertNull(errorResponse.getMessage());
        assertEquals(0, errorResponse.getStatusCode());
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor with all arguments
        ErrorResponse errorResponse = new ErrorResponse(404, "Not Found");

        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatusCode());
        assertEquals("Not Found", errorResponse.getMessage());
    }

    @Test
    void testConstructorWithMessageOnly() {
        // Testing the constructor with message only
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error");

        assertNotNull(errorResponse);
        assertEquals("Internal Server Error", errorResponse.getMessage());
        assertEquals(0, errorResponse.getStatusCode()); // Default value
    }

    @Test
    void testSettersAndGetters() {
        // Testing setters and getters
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatusCode(500);
        errorResponse.setMessage("Server Error");

        assertEquals(500, errorResponse.getStatusCode());
        assertEquals("Server Error", errorResponse.getMessage());
    }
}
