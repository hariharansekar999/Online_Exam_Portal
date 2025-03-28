package com.cts.onlineexamportall.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cts.onlineexamportall.dao.UserDAO;
import com.cts.onlineexamportall.model.User;
import com.cts.onlineexamportall.model.UserPrincipal;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Mocking the User object returned by the DAO
        User mockUser = new User();
        mockUser.setUserName("johndoe");
        mockUser.setPassword("securepassword");

        when(userDAO.findByUserName("johndoe")).thenReturn(mockUser);

        // Call the service
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("johndoe");

        // Verify the results
        assertNotNull(userDetails);
        assertEquals("johndoe", userDetails.getUsername());
        assertEquals("securepassword", userDetails.getPassword());
        assertTrue(userDetails instanceof UserPrincipal);

        verify(userDAO, times(1)).findByUserName("johndoe");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Simulate user not found
        when(userDAO.findByUserName("unknownUser")).thenReturn(null);

        // Call the service and verify it throws an exception
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknownUser");
        });

        assertEquals("No user found with the username: unknownUser", exception.getMessage());
        verify(userDAO, times(1)).findByUserName("unknownUser");
    }

    @Test
    void testLoadUserByUsername_DAOException() {
        // Simulate an exception in the DAO layer
        when(userDAO.findByUserName("johndoe")).thenThrow(new RuntimeException("Database error"));

        // Call the service and verify it propagates the exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            customUserDetailsService.loadUserByUsername("johndoe");
        });

        assertEquals("Database error", exception.getMessage());
        verify(userDAO, times(1)).findByUserName("johndoe");
    }
}
