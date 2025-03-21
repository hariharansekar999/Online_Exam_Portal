package com.cts.onlineexamportall.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.onlineexamportall.dto.ExamResponseDTO;
import com.cts.onlineexamportall.exception.UserNotFoundException;
import com.cts.onlineexamportall.model.Report;
import com.cts.onlineexamportall.model.User;
import com.cts.onlineexamportall.service.ExamService;
import com.cts.onlineexamportall.service.ReportService;
import com.cts.onlineexamportall.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private UserService userService;

    @SuppressWarnings("removal")
	@MockBean
    private ExamService examService;

    @SuppressWarnings("removal")
	@MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test for getProfile endpoint
    @Test
    void testGetProfile_Success() throws Exception {
        User user = new User();
        user.setUserName("johndoe");

        when(userService.getProfile("johndoe")).thenReturn(user);

        mockMvc.perform(get("/student/profile").param("username", "johndoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userName").value("johndoe"));
    }

    @Test
    void testGetProfile_UserNotFound() throws Exception {
        when(userService.getProfile("unknownUser")).thenThrow(new UserNotFoundException("User does not exist"));

        mockMvc.perform(get("/student/profile").param("username", "unknownUser"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("User does not exist"));
    }

    // Test for submitResponse endpoint
    @Test
    void testSubmitResponse_Success() throws Exception {
        ExamResponseDTO responseDTO = new ExamResponseDTO();
        responseDTO.setExamId(1L);
        responseDTO.setUserName("johndoe");

        doNothing().when(examService).submitResponse(responseDTO);

        when(userService.userExists("johndoe")).thenReturn(true);
        when(examService.examExists(1L)).thenReturn(true);
        when(examService.responseExist(1L, "johndoe")).thenReturn(false);

        mockMvc.perform(post("/student/submitResponse")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("The response has been submitted!"));
    }

    @Test
    void testSubmitResponse_UserNotFound() throws Exception {
        ExamResponseDTO responseDTO = new ExamResponseDTO();
        responseDTO.setExamId(1L);
        responseDTO.setUserName("unknownUser");

        when(userService.userExists("unknownUser")).thenReturn(false);

        mockMvc.perform(post("/student/submitResponse")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("User does not exist"));
    }

    @Test
    void testSubmitResponse_ExamNotFound() throws Exception {
        ExamResponseDTO responseDTO = new ExamResponseDTO();
        responseDTO.setExamId(999L);
        responseDTO.setUserName("johndoe");

        when(userService.userExists("johndoe")).thenReturn(true);
        when(examService.examExists(999L)).thenReturn(false);

        mockMvc.perform(post("/student/submitResponse")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Exam with Id 999 does not exist"));
    }

    @Test
    void testSubmitResponse_DuplicateAttempt() throws Exception {
        ExamResponseDTO responseDTO = new ExamResponseDTO();
        responseDTO.setExamId(1L);
        responseDTO.setUserName("johndoe");

        when(userService.userExists("johndoe")).thenReturn(true);
        when(examService.examExists(1L)).thenReturn(true);
        when(examService.responseExist(1L, "johndoe")).thenReturn(true);

        mockMvc.perform(post("/student/submitResponse")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("You are not allowed to re-attempt this exam"));
    }

    // Test for getMethodName (fetching reports) endpoint
    @Test
    void testGetReports_Success() throws Exception {
        Report report1 = new Report();
        report1.setUserName("johndoe");
        report1.setExamId(1L);
        report1.setScore(85.0);

        Report report2 = new Report();
        report2.setUserName("johndoe");
        report2.setExamId(2L);
        report2.setScore(90.0);

        List<Report> reports = Arrays.asList(report1, report2);

        when(reportService.getReportsByUserName("johndoe")).thenReturn(reports);

        mockMvc.perform(get("/student/report/johndoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].examId").value(1))
                .andExpect(jsonPath("$.data[1].examId").value(2))
                .andExpect(jsonPath("$.data[0].score").value(85.0))
                .andExpect(jsonPath("$.data[1].score").value(90.0));
    }

    @Test
    void testGetReports_UserNotFound() throws Exception {
        when(reportService.getReportsByUserName("unknownUser")).thenThrow(new UserNotFoundException("Reports not found"));

        mockMvc.perform(get("/student/report/unknownUser"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Reports not found"));
    }
}
