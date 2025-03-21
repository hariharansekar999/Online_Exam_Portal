package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class DuplicateAttemptExceptionTest {

    @Test
    void testDefaultConstructor() {
        DuplicateAttemptException exception = new DuplicateAttemptException();
        
        assertNotNull(exception);
        assertNull(exception.getMessage()); // Default message should be null
    }

    @Test
    void testParameterizedConstructor() {
        String errorMessage = "You have already attempted this exam";
        DuplicateAttemptException exception = new DuplicateAttemptException(errorMessage);
        
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage()); // Should match the provided message
    }
}
