package com.cts.onlineexamportall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cts.onlineexamportall.model.StudentExamResponse;

@DataJpaTest
public class StudentExamResponseDAOTest {

    @Autowired
    private StudentExamResponseDAO studentExamResponseDAO;

    @BeforeEach
    void setUp() {
        // Populate the in-memory database with sample data
        StudentExamResponse response1 = new StudentExamResponse();
        response1.setExamId(1L);
        response1.setUserName("johndoe");
        response1.setQuestionId(101L);
        response1.setAnswer("A");
        studentExamResponseDAO.save(response1);

        StudentExamResponse response2 = new StudentExamResponse();
        response2.setExamId(1L);
        response2.setUserName("johndoe");
        response2.setQuestionId(102L);
        response2.setAnswer("B");
        studentExamResponseDAO.save(response2);

        StudentExamResponse response3 = new StudentExamResponse();
        response3.setExamId(2L);
        response3.setUserName("janedoe");
        response3.setQuestionId(201L);
        response3.setAnswer("C");
        studentExamResponseDAO.save(response3);
    }

    @Test
    void testFindByExamIdAndUserName_Success() {
        // Verify that responses exist for the given exam ID and username
        List<StudentExamResponse> responses = studentExamResponseDAO.findByExamIdAndUserName(1L, "johndoe");

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertTrue(responses.stream().allMatch(r -> r.getExamId() == 1L && r.getUserName().equals("johndoe")));
    }

    @Test
    void testFindByExamIdAndUserName_NoResponsesFound() {
        // Verify that an empty list is returned when no responses exist for the given exam ID and username
        List<StudentExamResponse> responses = studentExamResponseDAO.findByExamIdAndUserName(3L, "unknownuser");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testFindByExamIdAndUserNameWithLogging_Success() {
        // Test the default method with logging for a valid exam ID and username
        List<StudentExamResponse> responses = studentExamResponseDAO.findByExamIdAndUserNameWithLogging(1L, "johndoe");

        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    void testFindByExamIdAndUserNameWithLogging_NoResponsesFound() {
        // Test the default method with logging for a nonexistent combination
        List<StudentExamResponse> responses = studentExamResponseDAO.findByExamIdAndUserNameWithLogging(99L, "unknownuser");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testDeleteByExamIdAndUserName_Success() {
        // Verify deletion of responses for the specified exam ID and username
        studentExamResponseDAO.deleteByExamIdAndUserName(1L, "johndoe");

        List<StudentExamResponse> responsesAfterDelete = studentExamResponseDAO.findByExamIdAndUserName(1L, "johndoe");

        assertNotNull(responsesAfterDelete);
        assertTrue(responsesAfterDelete.isEmpty());
    }

    @Test
    void testDeleteByExamIdAndUserNameWithLogging_Success() {
        // Test the default delete method with logging
        studentExamResponseDAO.deleteByExamIdAndUserNameWithLogging(1L, "johndoe");

        List<StudentExamResponse> responsesAfterDelete = studentExamResponseDAO.findByExamIdAndUserName(1L, "johndoe");

        assertNotNull(responsesAfterDelete);
        assertTrue(responsesAfterDelete.isEmpty());
    }
}
