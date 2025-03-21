package com.cts.onlineexamportall.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cts.onlineexamportall.dao.UserDAO;
import com.cts.onlineexamportall.enums.Role;
import com.cts.onlineexamportall.exception.UserNotFoundException;
import com.cts.onlineexamportall.model.User;

public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtilService jwtUtilService;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder(12);
    }

    // Test for userExists
    @Test
    void testUserExists_True() {
        User user = new User();
        user.setUserName("johndoe");

        when(userDAO.findByUserName("johndoe")).thenReturn(user);

        boolean exists = userService.userExists("johndoe");

        assertTrue(exists);
        verify(userDAO, times(1)).findByUserName("johndoe");
    }

    @Test
    void testUserExists_False() {
        when(userDAO.findByUserName("unknownUser")).thenReturn(null);

        boolean exists = userService.userExists("unknownUser");

        assertFalse(exists);
        verify(userDAO, times(1)).findByUserName("unknownUser");
    }

    // Test for userEmailExists
    @Test
    void testUserEmailExists_True() {
        User user = new User();
        user.setUserName("johndoe");
        user.setEmail("johndoe@example.com");

        when(userDAO.findByUserName("johndoe")).thenReturn(user);

        boolean exists = userService.userEmailExists("johndoe", "johndoe@example.com");

        assertTrue(exists);
    }

    @Test
    void testUserEmailExists_False() {
        User user = new User();
        user.setUserName("johndoe");
        user.setEmail("incorrect@example.com");

        when(userDAO.findByUserName("johndoe")).thenReturn(user);

        boolean exists = userService.userEmailExists("johndoe", "johndoe@example.com");

        assertFalse(exists);
    }

    // Test for passwordMatch
    @Test
    void testPasswordMatch_True() {
        User user = new User();
        user.setPassword(encoder.encode("securepassword"));

        when(userDAO.findByUserName("johndoe")).thenReturn(user);

        boolean matches = userService.passwordMatch("johndoe", "securepassword");

        assertTrue(matches);
    }

    @Test
    void testPasswordMatch_False() {
        User user = new User();
        user.setPassword(encoder.encode("securepassword"));

        when(userDAO.findByUserName("johndoe")).thenReturn(user);

        boolean matches = userService.passwordMatch("johndoe", "wrongpassword");

        assertFalse(matches);
    }

    // Test for register
    @Test
    void testRegister() {
        User user = new User();
        user.setUserName("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("securepassword");
        user.setRoles(Collections.singleton(Role.STUDENT));

        when(userDAO.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.register("johndoe", "johndoe@example.com", "securepassword", Role.STUDENT);

        assertNotNull(registeredUser);
        assertEquals("johndoe", registeredUser.getUserName());
        verify(userDAO, times(1)).save(any(User.class));
    }

    // Test for verify
    @Test
    void testVerify_Success() {
        User user = new User();
        user.setUserName("johndoe");
        user.setUserId(UUID.randomUUID());

        when(authManager.authenticate(any(Authentication.class))).thenReturn(mock(Authentication.class));
        when(userDAO.findByUserName("johndoe")).thenReturn(user);
        when(jwtUtilService.generateToken(user.getUserId(), "johndoe")).thenReturn("mockToken");

        String token = userService.verify("johndoe", "securepassword");

        assertNotNull(token);
        assertEquals("mockToken", token);
    }

    @Test
    void testVerify_InvalidCredentials() {
        when(authManager.authenticate(any(Authentication.class))).thenThrow(new UserNotFoundException("Invalid Login Credentials..."));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.verify("johndoe", "wrongpassword");
        });

        assertEquals("Invalid Login Credentials...", exception.getMessage());
    }

    // Test for getAllUser
    @Test
    void testGetAllUser() {
        User user1 = new User();
        user1.setUserName("johndoe");

        User user2 = new User();
        user2.setUserName("janedoe");

        when(userDAO.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUser();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userDAO, times(1)).findAll();
    }

    // Test for deleteByUsername
    @Test
    void testDeleteByUsername_Success() {
        User user = new User();
        user.setUserName("johndoe");

        when(userDAO.findByUserName("johndoe")).thenReturn(user);
        doNothing().when(userDAO).delete(user);

        userService.deleteByUsername("johndoe");

        verify(userDAO, times(1)).findByUserName("johndoe");
        verify(userDAO, times(1)).delete(user);
    }

    @Test
    void testDeleteByUsername_UserNotFound() {
        when(userDAO.findByUserName("unknownUser")).thenReturn(null);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteByUsername("unknownUser");
        });

        assertEquals("User with username not found", exception.getMessage());
    }
}
