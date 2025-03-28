package com.cts.onlineexamportall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cts.onlineexamportall.model.User;

@DataJpaTest
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        // Prepare sample data in the in-memory database
        User user1 = new User();
        user1.setUserId(UUID.randomUUID());
        user1.setUserName("johndoe");
        user1.setEmail("johndoe@example.com");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setUserId(UUID.randomUUID());
        user2.setUserName("janedoe");
        user2.setEmail("janedoe@example.com");
        user2.setPassword("password456");

        userDAO.save(user1).getUserId();
        userDAO.save(user2);
    }

    @Test
    void testFindByUserName_Success() {
        // Verify that the user exists with the given username
        User user = userDAO.findByUserName("johndoe");

        assertNotNull(user);
        assertEquals("johndoe", user.getUserName());
        assertEquals("johndoe@example.com", user.getEmail());
    }

    @Test
    void testFindByUserName_NotFound() {
        // Verify that null is returned for a nonexistent username
        User user = userDAO.findByUserName("unknownuser");

        assertNull(user);
    }

    @Test
    void testFindByUserNameWithLogging_Success() {
        // Test the default method with logging for a valid username
        User user = userDAO.findByUserNameWithLogging("janedoe");

        assertNotNull(user);
        assertEquals("janedoe", user.getUserName());
        assertEquals("janedoe@example.com", user.getEmail());
    }

    @Test
    void testFindByUserNameWithLogging_NotFound() {
        // Test the default method with logging for a nonexistent username
        User user = userDAO.findByUserNameWithLogging("nonexistentuser");

        assertNull(user);
    }

    @Test
    void testFindByEmail_Success() {
        // Verify that the user exists with the given email
        Optional<User> user = userDAO.findByEmail("johndoe@example.com");

        assertTrue(user.isPresent());
        assertEquals("johndoe@example.com", user.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        // Verify that an empty Optional is returned for a nonexistent email
        Optional<User> user = userDAO.findByEmail("unknown@example.com");

        assertFalse(user.isPresent());
    }

    @Test
    void testFindByEmailWithLogging_Success() {
        // Test the default method with logging for a valid email
        Optional<User> user = userDAO.findByEmailWithLogging("janedoe@example.com");

        assertTrue(user.isPresent());
        assertEquals("janedoe@example.com", user.get().getEmail());
    }

    @Test
    void testFindByEmailWithLogging_NotFound() {
        // Test the default method with logging for a nonexistent email
        Optional<User> user = userDAO.findByEmailWithLogging("nonexistent@example.com");

        assertFalse(user.isPresent());
    }
}
