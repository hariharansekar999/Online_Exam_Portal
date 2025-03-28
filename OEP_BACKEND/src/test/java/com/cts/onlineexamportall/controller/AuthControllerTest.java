package com.cts.onlineexamportall.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.onlineexamportall.dto.LoginRequestDTO;
import com.cts.onlineexamportall.dto.UserRequest;
import com.cts.onlineexamportall.enums.Role;
import com.cts.onlineexamportall.model.User;
import com.cts.onlineexamportall.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginUser_Success() throws Exception {
        // Mocking userService
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUserName("johndoe");
        loginRequest.setPassword("password123");

        when(userService.userExists(loginRequest.getUserName())).thenReturn(true);
        when(userService.passwordMatch(loginRequest.getUserName(), loginRequest.getPassword())).thenReturn(true);
        when(userService.verify(loginRequest.getUserName(), loginRequest.getPassword())).thenReturn("mock-token");

        // Perform POST request
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").value("mock-token"));
    }

    @Test
    void testLoginUser_UserNotFound() throws Exception {
        // Mocking userService
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUserName("unknownUser");
        loginRequest.setPassword("password123");

        when(userService.userExists(loginRequest.getUserName())).thenReturn(false);

        // Perform POST request
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorMessage").value("User does not exist!"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Mocking userService
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("johndoe");
        userRequest.setEmail("johndoe@example.com");
        userRequest.setPassword("password123");
        userRequest.setRole(Role.STUDENT);

        User mockUser = new User();
        mockUser.setUserName("johndoe");
        mockUser.setEmail("johndoe@example.com");
        mockUser.setRoles(Set.of(Role.STUDENT));

        when(userService.userExists(userRequest.getUserName())).thenReturn(false);
        when(userService.userEmailExists(userRequest.getUserName(), userRequest.getEmail())).thenReturn(true);
        when(userService.register(userRequest.getUserName(), userRequest.getEmail(), userRequest.getPassword(), userRequest.getRole()))
                .thenReturn(mockUser);

        // Perform POST request
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.userName").value("johndoe"));
    }

    @Test
    void testRegisterUser_UserExists() throws Exception {
        // Mocking userService
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("johndoe");
        userRequest.setEmail("johndoe@example.com");
        userRequest.setPassword("password123");
        userRequest.setRole(Role.STUDENT);

        when(userService.userExists(userRequest.getUserName())).thenReturn(true);

        // Perform POST request
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorMessage").value("User already exists!"));
    }

    @Test
    void testUpdatePassword_Success() throws Exception {
        // Mocking userService
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("johndoe");
        userRequest.setEmail("johndoe@example.com");
        userRequest.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setUserName("johndoe");
        updatedUser.setEmail("johndoe@example.com");

        when(userService.userExists(userRequest.getUserName())).thenReturn(true);
        when(userService.userEmailExists(userRequest.getUserName(), userRequest.getEmail())).thenReturn(true);
        when(userService.updatePassword(userRequest.getUserName(), userRequest.getPassword(), "newPassword"))
                .thenReturn(updatedUser);

        // Perform PUT request
        mockMvc.perform(put("/api/auth/updatePassword")
                .contentType("application/json")
                .queryParam("newPassword", "newPassword")
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.userName").value("johndoe"));
    }
}
