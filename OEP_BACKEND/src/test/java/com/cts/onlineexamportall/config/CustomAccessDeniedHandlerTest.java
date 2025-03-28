package com.cts.onlineexamportall.config;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandlerTest {

    @InjectMocks
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mocked PrintWriter and StringWriter
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void testHandleLogsWarning() throws Exception {
        // Mock PrintWriter and StringWriter
        when(response.getWriter()).thenReturn(printWriter);

        // Simulate an AccessDeniedException
        AccessDeniedException exception = new AccessDeniedException("Access Denied");

        // Call the handler method
        customAccessDeniedHandler.handle(request, response, exception);

        // Verify response properties
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Flush and assert the response content
        printWriter.flush();
        String responseContent = stringWriter.toString(); // Captured response
        System.out.println("Captured Response: " + responseContent); // Debugging output
        assertTrue(responseContent.contains("\"error\":\"Access Denied\""), "Response should contain the error message");
    }


}

