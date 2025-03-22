package com.cts.onlineexamportall.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cts.onlineexamportall.dto.Response;
import com.cts.onlineexamportall.exception.PasswordMisMatchException;
import com.cts.onlineexamportall.model.User;
import com.cts.onlineexamportall.enums.Role;
import com.cts.onlineexamportall.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setUserId(UUID.randomUUID());
        user1.setUserName("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setRoles(new HashSet<>(Arrays.asList(Role.STUDENT)));

        User user2 = new User();
        user2.setUserId(UUID.randomUUID());
        user2.setUserName("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUser()).thenReturn(users);

        mockMvc.perform(get("/admin/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data[0].userName").value("user1"))
                .andExpect(jsonPath("$.data[1].userName").value("user2"));
    }

    @Test
    public void testDeleteByUsername() throws Exception {
        String username = "user1";

        doNothing().when(userService).deleteByUsername(username);

        mockMvc.perform(delete("/admin/delete/{username}", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data").value("User deleted successfully"));
    }

    @Test
    public void testDeleteByUsername_PasswordMisMatchException() throws Exception {
        String username = "user1";

        doThrow(new PasswordMisMatchException("Password mismatch")).when(userService).deleteByUsername(username);

        mockMvc.perform(delete("/admin/delete/{username}", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Password mismatch"));
    }
}
