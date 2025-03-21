package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class QuestionNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Testing the default constructor
        QuestionNotFoundException exception = new QuestionNotFoundException();

        assertNotNull(exception); // Ensure the exception is not null
        assertNull(exception.getMessage()); // Default message should be null
    }

    @Test
    void testParameterizedConstructor() {
        // Testing the parameterized constructor
        String errorMessage = "Question not found";
        QuestionNotFoundException exception = new QuestionNotFoundException(errorMessage);

        assertNotNull(exception); // Ensure the exception is not null
        assertEquals(errorMessage, exception.getMessage()); // The error message should match
    }
}
