package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ExamExistsExceptionTest {

    @Test
    void testDefaultConstructor() {
        ExamExistsException exception = new ExamExistsException();

        assertNotNull(exception);
        assertNull(exception.getMessage()); // Default message should be null
    }

    @Test
    void testParameterizedConstructor() {
        String errorMessage = "Exam already exists";
        ExamExistsException exception = new ExamExistsException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage()); // Should match the provided message
    }
}
