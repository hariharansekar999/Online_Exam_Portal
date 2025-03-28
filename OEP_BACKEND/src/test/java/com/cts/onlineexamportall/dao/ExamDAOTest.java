package com.cts.onlineexamportall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cts.onlineexamportall.model.Exam;

@DataJpaTest
public class ExamDAOTest {

    @Autowired
    private ExamDAO examDAO;

    @BeforeEach
    void setUp() {
        // Prepare sample data in the in-memory database
        Exam exam1 = new Exam();
        exam1.setTitle("Java Basics");
        exam1.setDescription("Exam on Java fundamentals");
        exam1.setTotalMarks(100);
        exam1.setDuration(60);
        examDAO.save(exam1);

        Exam exam2 = new Exam();
        exam2.setTitle("Python Basics");
        exam2.setDescription("Exam on Python programming");
        exam2.setTotalMarks(100);
        exam2.setDuration(60);
        examDAO.save(exam2);
    }

    @Test
    void testFindByTitle_Success() {
        // Test case where the title exists in the database
        Exam exam = examDAO.findByTitle("Java Basics");

        assertNotNull(exam);
        assertEquals("Java Basics", exam.getTitle());
        assertEquals("Exam on Java fundamentals", exam.getDescription());
    }

    @Test
    void testFindByTitle_NotFound() {
        // Test case where the title does not exist in the database
        Exam exam = examDAO.findByTitle("C++ Basics");

        assertNull(exam);
    }

    @Test
    void testFindByTitleWithLogging_Success() {
        // Testing the default method with logging
        Exam exam = examDAO.findByTitleWithLogging("Python Basics");

        assertNotNull(exam);
        assertEquals("Python Basics", exam.getTitle());
        assertEquals("Exam on Python programming", exam.getDescription());
    }

    @Test
    void testFindByTitleWithLogging_NotFound() {
        // Testing the default method with a title that doesn't exist
        Exam exam = examDAO.findByTitleWithLogging("C++ Basics");

        assertNull(exam); // Verify that null is returned when not found
    }
}
