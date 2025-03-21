package com.cts.onlineexamportall.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.onlineexamportall.dto.Response;
import com.cts.onlineexamportall.exception.PasswordMisMatchException;
import com.cts.onlineexamportall.model.User;
import com.cts.onlineexamportall.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<Response<?>> getAllUsers() {
        try {
            logger.info("Fetching all users");
            List<User> user = userService.getAllUser();
            Response<List<User>> response = new Response<>(true, HttpStatus.OK, user, null);
            logger.info("Successfully fetched all users");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            logger.error("Error fetching users: {}", ex.getMessage());
            Response<String> response = new Response<>(false, HttpStatus.BAD_REQUEST, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Response<?>> deleteByUsername(@PathVariable String username) {
        try {
            logger.info("Deleting user with username: {}", username);
            userService.deleteByUsername(username);
            Response<String> response = new Response<>(true, HttpStatus.OK, "User deleted successfully", null);
            logger.info("Successfully deleted user with username: {}", username);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PasswordMisMatchException ex) {
            logger.error("Error deleting user: {}", ex.getMessage());
            Response<String> response = new Response<>(false, HttpStatus.BAD_REQUEST, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}