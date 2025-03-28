package com.cts.onlineexamportall.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordEncoder() {
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/api/auth/register"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testRoleBasedAccessControl() throws Exception {
        // Access as ADMIN
        mockMvc.perform(get("/api/auth/users").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        // Access as STUDENT (should be forbidden)
        mockMvc.perform(get("/api/auth/users").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessDeniedHandler() throws Exception {
        mockMvc.perform(get("/admin").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"message\": \"You do not have the necessary permissions to access this resource.\"}"));
    }
}
