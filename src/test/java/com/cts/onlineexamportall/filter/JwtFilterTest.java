package com.cts.onlineexamportall.filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.cts.onlineexamportall.service.CustomUserDetailsService;
import com.cts.onlineexamportall.service.JwtUtilService;

import jakarta.servlet.FilterChain;

public class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtilService jwtUtilService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testDoFilterInternal_ValidToken() throws Exception {
        // Mock Authorization header with a valid Bearer token
        String token = "mockToken";
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        UserDetails userDetails = User.builder().username(username).password("password").roles("USER").build();

        request.addHeader("Authorization", "Bearer " + token);
        when(jwtUtilService.extractUserName(token)).thenReturn(username);
        when(jwtUtilService.extractUserId(token)).thenReturn(userId.toString());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtilService.validateToken(token, userDetails)).thenReturn(true);

        // Execute the filter
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Validate that authentication is successfully set in the SecurityContext
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);

        // Verify interactions with mocked dependencies
        verify(jwtUtilService, times(1)).extractUserName(token);
        verify(jwtUtilService, times(1)).extractUserId(token);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(jwtUtilService, times(1)).validateToken(token, userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws Exception {
        // Mock Authorization header with an invalid Bearer token
        String token = "invalidToken";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtUtilService.extractUserName(token)).thenThrow(new IllegalArgumentException("Invalid token"));

        // Execute the filter
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Ensure SecurityContext is not set
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify interactions with mocked dependencies
        verify(jwtUtilService, times(1)).extractUserName(token);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_MissingAuthorizationHeader() throws Exception {
        // No Authorization header in the request
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Ensure SecurityContext is not set
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify the filter chain was still executed
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ExpiredToken() throws Exception {
        // Mock Authorization header with an expired Bearer token
        String token = "expiredToken";
        String username = "johndoe";
        UserDetails userDetails = User.builder().username(username).password("password").roles("USER").build();

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtUtilService.extractUserName(token)).thenReturn(username);
        when(jwtUtilService.validateToken(token, userDetails)).thenReturn(false);

        // Execute the filter
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Ensure SecurityContext is not set
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify interactions with mocked dependencies
        verify(jwtUtilService, times(1)).extractUserName(token);
        verify(jwtUtilService, times(1)).validateToken(token, userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
