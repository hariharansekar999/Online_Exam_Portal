package com.cts.onlineexamportall.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.onlineexamportall.dao.QuestionDAO;
import com.cts.onlineexamportall.dto.QuestionDTO;
import com.cts.onlineexamportall.exception.QuestionNotFoundException;
import com.cts.onlineexamportall.model.Question;

public class QuestionServiceTest {

    @Mock
    private QuestionDAO questionDAO;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getAllQuestions
    @Test
    void testGetAllQuestions_Success() {
        Question question1 = new Question();
        question1.setDescription("What is Java?");
        Question question2 = new Question();
        question2.setDescription("Explain OOP.");

        when(questionDAO.findAll()).thenReturn(Arrays.asList(question1, question2));

        List<Question> questions = questionService.getAllQuestions();

        assertNotNull(questions);
        assertEquals(2, questions.size());
        verify(questionDAO, times(1)).findAll();
    }

    @Test
    void testGetAllQuestions_NoQuestionsFound() {
        when(questionDAO.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            questionService.getAllQuestions();
        });

        assertEquals("No questions found", exception.getMessage());
        verify(questionDAO, times(1)).findAll();
    }

    // Test for createMultipleQuestions
    @Test
    void testCreateMultipleQuestions_Success() {
        QuestionDTO question1 = new QuestionDTO();
        question1.setDescription("What is encapsulation?");
        question1.setCategory("Programming");

        QuestionDTO question2 = new QuestionDTO();
        question2.setDescription("What is inheritance?");
        question2.setCategory("Programming");

        List<QuestionDTO> questionDTOs = Arrays.asList(question1, question2);

        doNothing().when(questionDAO).saveAll(anyList());

        questionService.createMultipleQuestions(questionDTOs);

        verify(questionDAO, times(1)).saveAll(anyList());
    }

    // Test for updateQuestion
    @Test
    void testUpdateQuestion_Success() {
        Question existingQuestion = new Question();
        existingQuestion.setDescription("Old Question");

        QuestionDTO updatedQuestionDTO = new QuestionDTO();
        updatedQuestionDTO.setDescription("Updated Question");

        when(questionDAO.findById(1L)).thenReturn(Optional.of(existingQuestion));
        when(questionDAO.save(any(Question.class))).thenReturn(existingQuestion);

        Question updatedQuestion = questionService.updateQuestion(1L, updatedQuestionDTO);

        assertNotNull(updatedQuestion);
        assertEquals("Updated Question", updatedQuestion.getDescription());
        verify(questionDAO, times(1)).findById(1L);
        verify(questionDAO, times(1)).save(existingQuestion);
    }

    @Test
    void testUpdateQuestion_QuestionNotFound() {
        QuestionDTO updatedQuestionDTO = new QuestionDTO();
        updatedQuestionDTO.setDescription("Updated Question");

        when(questionDAO.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(QuestionNotFoundException.class, () -> {
            questionService.updateQuestion(1L, updatedQuestionDTO);
        });

        assertEquals("Question not found", exception.getMessage());
        verify(questionDAO, times(1)).findById(1L);
    }

    // Test for createByCategory
    @Test
    void testCreateByCategory_Success() {
        QuestionDTO question1 = new QuestionDTO();
        question1.setDescription("What is abstraction?");
        question1.setCategory("Programming");

        QuestionDTO question2 = new QuestionDTO();
        question2.setDescription("What is polymorphism?");
        question2.setCategory("Programming");

        List<QuestionDTO> questionDTOs = Arrays.asList(question1, question2);

        doNothing().when(questionDAO).saveAll(anyList());

        questionService.createByCategory("Programming", questionDTOs);

        verify(questionDAO, times(1)).saveAll(anyList());
    }

    // Test for deleteQuestion
    @Test
    void testDeleteQuestion_Success() {
        Question question = new Question();
        question.setDescription("Sample Question");

        when(questionDAO.findById(1L)).thenReturn(Optional.of(question));
        doNothing().when(questionDAO).delete(question);

        questionService.deleteQuestion(1L);

        verify(questionDAO, times(1)).findById(1L);
        verify(questionDAO, times(1)).delete(question);
    }

    @Test
    void testDeleteQuestion_QuestionNotFound() {
        when(questionDAO.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(QuestionNotFoundException.class, () -> {
            questionService.deleteQuestion(1L);
        });

        assertEquals("Question not found", exception.getMessage());
        verify(questionDAO, times(1)).findById(1L);
    }

    // Test for createQuestion
    @Test
    void testCreateQuestion_Success() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setDescription("What is Java?");
        questionDTO.setCategory("Programming");

        Question question = new Question();
        question.setDescription("What is Java?");
        question.setCategory("Programming");

        when(questionDAO.save(any(Question.class))).thenReturn(question);

        Question createdQuestion = questionService.createQuestion(questionDTO);

        assertNotNull(createdQuestion);
        assertEquals("What is Java?", createdQuestion.getDescription());
        verify(questionDAO, times(1)).save(any(Question.class));
    }
}
