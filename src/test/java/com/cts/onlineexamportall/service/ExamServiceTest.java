package com.cts.onlineexamportall.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.onlineexamportall.dao.ExamDAO;
import com.cts.onlineexamportall.dao.QuestionDAO;
import com.cts.onlineexamportall.dao.ReportDAO;
import com.cts.onlineexamportall.dao.StudentExamResponseDAO;
import com.cts.onlineexamportall.dao.UserDAO;
import com.cts.onlineexamportall.dto.ExamDTO;
import com.cts.onlineexamportall.model.Exam;
import com.cts.onlineexamportall.model.Question;
import com.cts.onlineexamportall.model.Report;
import com.cts.onlineexamportall.model.StudentExamResponse;

public class ExamServiceTest {

    @Mock
    private ExamDAO examDAO;

    @Mock
    private QuestionDAO questionDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private ReportDAO reportDAO;

    @Mock
    private StudentExamResponseDAO responseDAO;

    @InjectMocks
    private ExamService examService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for examExists
    @Test
    void testExamExists_True() {
        when(examDAO.findById(1L)).thenReturn(Optional.of(new Exam()));

        boolean exists = examService.examExists(1L);

        assertTrue(exists);
        verify(examDAO, times(1)).findById(1L);
    }

    @Test
    void testExamExists_False() {
        when(examDAO.findById(1L)).thenReturn(Optional.empty());

        boolean exists = examService.examExists(1L);

        assertFalse(exists);
        verify(examDAO, times(1)).findById(1L);
    }

    // Test for examNameExists
    @Test
    void testExamNameExists_True() {
        when(examDAO.findByTitle("Java Basics")).thenReturn(new Exam());

        boolean exists = examService.examNameExists("Java Basics");

        assertTrue(exists);
        verify(examDAO, times(1)).findByTitle("Java Basics");
    }

    @Test
    void testExamNameExists_False() {
        when(examDAO.findByTitle("Python Basics")).thenReturn(null);

        boolean exists = examService.examNameExists("Python Basics");

        assertFalse(exists);
        verify(examDAO, times(1)).findByTitle("Python Basics");
    }

    // Test for getAllExams
    @Test
    void testGetAllExams_Success() {
        Exam exam1 = new Exam();
        exam1.setTitle("Java Basics");

        Exam exam2 = new Exam();
        exam2.setTitle("Python Basics");

        when(examDAO.findAll()).thenReturn(Arrays.asList(exam1, exam2));

        List<Exam> exams = examService.getAllExams();

        assertNotNull(exams);
        assertEquals(2, exams.size());
        verify(examDAO, times(1)).findAll();
    }

    @Test
    void testGetAllExams_NoExamsFound() {
        when(examDAO.findAll()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () -> examService.getAllExams());

        assertEquals("Failed to fetch exams", exception.getMessage());
        verify(examDAO, times(1)).findAll();
    }

    // Test for createExam
    @Test
    void testCreateExam_Success() {
        ExamDTO examDTO = new ExamDTO();
        examDTO.setTitle("Java Basics");
        examDTO.setDescription("A Java exam");
        examDTO.setTotalMarks(100);
        examDTO.setDuration(60);

        Question question = new Question();
        question.setCategory("Programming");

        when(questionDAO.findAllByCategory("Programming")).thenReturn(Arrays.asList(question));

        Exam createdExam = examService.createExam("Programming", examDTO);

        assertNotNull(createdExam);
        assertEquals("Java Basics", createdExam.getTitle());
        verify(examDAO, times(1)).save(any(Exam.class));
    }

    // Test for updateExam
    @Test
    void testUpdateExam_Success() {
        Exam existingExam = new Exam();
        existingExam.setTitle("Old Title");

        ExamDTO examDTO = new ExamDTO();
        examDTO.setTitle("New Title");
        examDTO.setDescription("Updated Description");

        when(examDAO.findById(1L)).thenReturn(Optional.of(existingExam));
        when(examDAO.save(any(Exam.class))).thenReturn(existingExam);

        Exam updatedExam = examService.updateExam(1L, examDTO);

        assertNotNull(updatedExam);
        assertEquals("New Title", updatedExam.getTitle());
        verify(examDAO, times(1)).findById(1L);
        verify(examDAO, times(1)).save(existingExam);
    }

    @Test
    void testUpdateExam_ExamNotFound() {
        ExamDTO examDTO = new ExamDTO();
        when(examDAO.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> examService.updateExam(1L, examDTO));

        assertEquals("Exam not found", exception.getMessage());
        verify(examDAO, times(1)).findById(1L);
    }

    // Test for generateReport
    @SuppressWarnings("deprecation")
    @Test
    void testGenerateReport_Success() {
        Exam exam = new Exam();
        exam.setTotalMarks(100);

        StudentExamResponse response1 = new StudentExamResponse();
        response1.setQuestionId(1L);
        response1.setAnswer("Correct");

        StudentExamResponse response2 = new StudentExamResponse();
        response2.setQuestionId(2L);
        response2.setAnswer("Incorrect");

        Question question1 = new Question();
        question1.setCorrectAnswer("Correct");

        Question question2 = new Question();
        question2.setCorrectAnswer("Correct");

        when(examDAO.findById(1L)).thenReturn(Optional.of(exam));
        when(responseDAO.findByExamIdAndUserName(1L, "johndoe")).thenReturn(Arrays.asList(response1, response2));
        when(questionDAO.getById(1L)).thenReturn(question1);
        when(questionDAO.getById(2L)).thenReturn(question2);

        Report report = examService.generateReport(1L, "johndoe");

        assertNotNull(report);
        assertEquals(50.0, report.getScore());
        verify(reportDAO, times(1)).save(any(Report.class));
    }
}
