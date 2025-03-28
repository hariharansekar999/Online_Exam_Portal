package com.cts.onlineexamportall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cts.onlineexamportall.model.Question;

@DataJpaTest
public class QuestionDAOTest {

    @Autowired
    private QuestionDAO questionDAO;

    @BeforeEach
    void setUp() {
        // Prepare sample data in the in-memory database
        Question question1 = new Question();
        question1.setDescription("What is Java?");
        question1.setCategory("Programming");
        question1.setDifficulty("Easy");
        question1.setCorrectAnswer("A programming language");
        questionDAO.save(question1);

        Question question2 = new Question();
        question2.setDescription("What is Python?");
        question2.setCategory("Programming");
        question2.setDifficulty("Easy");
        question2.setCorrectAnswer("Another programming language");
        questionDAO.save(question2);

        Question question3 = new Question();
        question3.setDescription("What is History?");
        question3.setCategory("General Knowledge");
        question3.setDifficulty("Medium");
        question3.setCorrectAnswer("The study of past events");
        questionDAO.save(question3);
    }

    @Test
    void testFindAllByCategory_Success() {
        // Test case where questions exist in the given category
        List<Question> questions = questionDAO.findAllByCategory("Programming");

        assertNotNull(questions);
        assertEquals(2, questions.size()); // Two questions in the "Programming" category
        assertTrue(questions.stream().allMatch(q -> q.getCategory().equals("Programming")));
    }

    @Test
    void testFindAllByCategory_NoQuestionsFound() {
        // Test case where no questions exist in the given category
        List<Question> questions = questionDAO.findAllByCategory("Mathematics");

        assertNotNull(questions); // Should return an empty list, not null
        assertTrue(questions.isEmpty());
    }

    @Test
    void testFindAllByCategoryWithLogging_Success() {
        // Testing the default method with logging
        List<Question> questions = questionDAO.findAllByCategoryWithLogging("Programming");

        assertNotNull(questions);
        assertEquals(2, questions.size()); // Two questions in the "Programming" category
        assertTrue(questions.stream().allMatch(q -> q.getCategory().equals("Programming")));
    }

    @Test
    void testFindAllByCategoryWithLogging_NoQuestionsFound() {
        // Testing the default method where no questions exist for the category
        List<Question> questions = questionDAO.findAllByCategoryWithLogging("Science");

        assertNotNull(questions); // Should return an empty list
        assertTrue(questions.isEmpty());
    }
}
