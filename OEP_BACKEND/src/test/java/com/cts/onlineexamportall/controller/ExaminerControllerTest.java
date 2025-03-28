package com.cts.onlineexamportall.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.onlineexamportall.dto.ExamDTO;
import com.cts.onlineexamportall.dto.QuestionDTO;
import com.cts.onlineexamportall.model.Exam;
import com.cts.onlineexamportall.model.Question;
import com.cts.onlineexamportall.service.ExamService;
import com.cts.onlineexamportall.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ExaminerController.class)
public class ExaminerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private QuestionService questionService;

    @SuppressWarnings("removal")
	@MockBean
    private ExamService examService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test for getAllQuestions endpoint
    @Test
    void testGetAllQuestions_Success() throws Exception {
        Question question1 = new Question();
        question1.setDescription("What is polymorphism?");
        Question question2 = new Question();
        question2.setDescription("What is inheritance?");

        List<Question> questions = Arrays.asList(question1, question2);

        when(questionService.getAllQuestions()).thenReturn(questions);

        mockMvc.perform(get("/examiner/allQuestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("What is polymorphism?"))
                .andExpect(jsonPath("$[1].description").value("What is inheritance?"));
    }

    // Test for createQuestion endpoint
    @Test
    void testCreateQuestion_Success() throws Exception {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setDescription("Explain OOP concepts.");
        questionDTO.setCategory("Programming");
        questionDTO.setDifficulty("Medium");
        questionDTO.setCorrectAnswer("Encapsulation, inheritance, polymorphism, abstraction.");

        Question createdQuestion = new Question();
        createdQuestion.setDescription("Explain OOP concepts.");

        when(questionService.createQuestion(questionDTO)).thenReturn(createdQuestion);

        mockMvc.perform(post("/examiner/createQuestion")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(questionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.description").value("Explain OOP concepts."));
    }

    // Test for createMultipleQuestions endpoint
    @Test
    void testCreateMultipleQuestions_Success() throws Exception {
        QuestionDTO question1 = new QuestionDTO();
        question1.setDescription("What is encapsulation?");
        question1.setCategory("Programming");
        question1.setDifficulty("Easy");
        question1.setCorrectAnswer("Encapsulation binds data and functions together.");

        QuestionDTO question2 = new QuestionDTO();
        question2.setDescription("What is abstraction?");
        question2.setCategory("Programming");
        question2.setDifficulty("Medium");
        question2.setCorrectAnswer("Abstraction hides implementation details.");

        List<QuestionDTO> questions = Arrays.asList(question1, question2);

        doNothing().when(questionService).createMultipleQuestions(questions);

        mockMvc.perform(post("/examiner/createMultipleQuestions")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(questions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("List of questions added successfully"));
    }

    // Test for updateQuestion endpoint
    @Test
    void testUpdateQuestion_Success() throws Exception {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setDescription("Updated Question Description");
        questionDTO.setCategory("Updated Category");

        Question updatedQuestion = new Question();
        updatedQuestion.setDescription("Updated Question Description");

        when(questionService.updateQuestion(eq(1L), any(QuestionDTO.class))).thenReturn(updatedQuestion);

        mockMvc.perform(put("/examiner/updateQuestion/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(questionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.description").value("Updated Question Description"));
    }

    // Test for deleteQuestion endpoint
    @Test
    void testDeleteQuestion_Success() throws Exception {
        doNothing().when(questionService).deleteQuestion(1L);

        mockMvc.perform(delete("/examiner/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Question deleted successfully!"));
    }

    // Test for getAllExams endpoint
    @Test
    void testGetAllExams_Success() throws Exception {
        Exam exam1 = new Exam();
        exam1.setTitle("Java Basics Exam");

        Exam exam2 = new Exam();
        exam2.setTitle("OOP Exam");

        List<Exam> exams = Arrays.asList(exam1, exam2);

        when(examService.getAllExams()).thenReturn(exams);

        mockMvc.perform(get("/examiner/allExams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Java Basics Exam"))
                .andExpect(jsonPath("$[1].title").value("OOP Exam"));
    }

    // Test for createExam endpoint
    @Test
    void testCreateExam_Success() throws Exception {
        ExamDTO examDTO = new ExamDTO();
        examDTO.setTitle("Java Basics");
        examDTO.setDescription("Exam on Java fundamentals");
        examDTO.setTotalMarks(100);
        examDTO.setDuration(90);

        Exam createdExam = new Exam();
        createdExam.setTitle("Java Basics");
        createdExam.setDescription("Exam on Java fundamentals");

        when(examService.examNameExists(examDTO.getTitle())).thenReturn(false);
        when(examService.createExam(eq("Programming"), eq(examDTO))).thenReturn(createdExam);

        mockMvc.perform(post("/examiner/createExam/Programming")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(examDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Java Basics"));
    }

    // Test for createExam when exam exists
    @Test
    void testCreateExam_ExamExists() throws Exception {
        ExamDTO examDTO = new ExamDTO();
        examDTO.setTitle("Java Basics");
        examDTO.setDescription("Exam on Java fundamentals");

        when(examService.examNameExists(examDTO.getTitle())).thenReturn(true);

        mockMvc.perform(post("/examiner/createExam/Programming")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(examDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("The given title already exists!"));
    }
}
