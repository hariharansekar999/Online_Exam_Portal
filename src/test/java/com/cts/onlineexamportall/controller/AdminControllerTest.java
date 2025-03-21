package com.cts.onlineexamportall.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.onlineexamportall.exception.PasswordMisMatchException;
import com.cts.onlineexamportall.model.User;
import com.cts.onlineexamportall.service.UserService;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private UserService userService;

    @Test
    void testGetAllUsers_Success() throws Exception {
        // Mock service response
        User user1 = new User();
        user1.setUserId(UUID.randomUUID());
        user1.setUserName("johndoe");
        user1.setEmail("johndoe@example.com");

        User user2 = new User();
        user2.setUserId(UUID.randomUUID());
        user2.setUserName("janedoe");
        user2.setEmail("janedoe@example.com");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUser()).thenReturn(users);

        // Perform GET request
        mockMvc.perform(get("/admin/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].userName").value("johndoe"))
                .andExpect(jsonPath("$.data[1].userName").value("janedoe"));
    }

    @Test
    void testGetAllUsers_BadRequest() throws Exception {
        // Simulate service throwing exception
        when(userService.getAllUser()).thenThrow(new IllegalArgumentException("Invalid request"));

        // Perform GET request
        mockMvc.perform(get("/admin/getAllUsers"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorMessage").value("Invalid request"));
    }

    @Test
    void testDeleteByUsername_Success() throws Exception {
        // Mock service behavior
        doNothing().when(userService).deleteByUsername("johndoe");

        // Perform DELETE request
        mockMvc.perform(delete("/admin/delete/johndoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").value("User deleted successfully"));
    }

    @Test
    void testDeleteByUsername_BadRequest() throws Exception {
        // Simulate PasswordMismatchException
        doThrow(new PasswordMisMatchException("Password mismatch")).when(userService).deleteByUsername("johndoe");

        // Perform DELETE request
        mockMvc.perform(delete("/admin/delete/johndoe"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorMessage").value("Password mismatch"));
    }
}
