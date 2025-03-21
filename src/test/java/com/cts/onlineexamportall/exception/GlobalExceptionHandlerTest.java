package com.cts.onlineexamportall.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User does not exist");
        
        ErrorResponse response = globalExceptionHandler.handleUserNotFoundException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("User does not exist", response.getMessage());
    }

    @Test
    void testHandleQuestionNotFoundException() {
        QuestionNotFoundException ex = new QuestionNotFoundException("Question not found");
        
        ErrorResponse response = globalExceptionHandler.handleQuestionNotFoundException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("Question not found", response.getMessage());
    }

    @Test
    void testHandlePasswordMismatchException() {
        PasswordMisMatchException ex = new PasswordMisMatchException("Password mismatch");
        
        ErrorResponse response = globalExceptionHandler.handlePasswordMissMatchException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("Password mismatch", response.getMessage());
    }

    @Test
    void testHandleReportNotFoundException() {
        ReportNotFoundException ex = new ReportNotFoundException("Report not found");
        
        ErrorResponse response = globalExceptionHandler.handleReportNotFoundException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("Report not found", response.getMessage());
    }

    @Test
    void testHandleUserExistsException() {
        UserExistsException ex = new UserExistsException("User already exists");
        
        ErrorResponse response = globalExceptionHandler.handleUserExistsException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
        assertEquals("User already exists", response.getMessage());
    }

    @Test
    void testHandleDuplicateAttemptException() {
        DuplicateAttemptException ex = new DuplicateAttemptException("Duplicate attempt detected");
        
        ErrorResponse response = globalExceptionHandler.handleDuplicateAttemptException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
        assertEquals("Duplicate attempt detected", response.getMessage());
    }

    @Test
    void testHandleBadCredentialsException() {
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");
        
        ErrorResponse response = globalExceptionHandler.handleBadCredential(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_GATEWAY.value(), response.getStatusCode());
        assertEquals("Invalid credentials", response.getMessage());
    }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate email");
        
        ErrorResponse response = globalExceptionHandler.handleDataIntegrityViolationException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
        assertEquals("A user with this email already exists. Please use a different email.", response.getMessage());
    }

    @Test
    void testHandleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath().toString()).thenReturn("email");
        when(violation.getMessage()).thenReturn("must be a valid email address");
        
        ConstraintViolationException ex = new ConstraintViolationException(Collections.singleton(violation));
        
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleConstraintViolationException(ex);
        
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Validation failed: \nemail: must be a valid email address", responseEntity.getBody().getMessage());
    }

    @Test
    void testHandleValidationException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult().getFieldErrors()).thenReturn(Collections.emptyList());

        ErrorResponse response = globalExceptionHandler.handleValidationException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals("Validation failed: ", response.getMessage());
    }

    @Test
    void testHandleExamNotFoundException() {
        ExamNotFoundException ex = new ExamNotFoundException("Exam not found");
        
        ErrorResponse response = globalExceptionHandler.handleExamNotFoundException(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("Exam not found", response.getMessage());
    }
}
