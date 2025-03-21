package com.cts.onlineexamportall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cts.onlineexamportall.model.Report;

@DataJpaTest
public class ReportDAOTest {

    @Autowired
    private ReportDAO reportDAO;

    @BeforeEach
    void setUp() {
        // Prepare sample data for testing in the in-memory database
        Report report1 = new Report();
        report1.setUserName("johndoe");
        report1.setExamId(1L);
        report1.setTotalQuestions(10);
        report1.setCorrectAnswers(8);
        report1.setScore(80.0);
        report1.setPercentage(80.0);
        reportDAO.save(report1);

        Report report2 = new Report();
        report2.setUserName("johndoe");
        report2.setExamId(2L);
        report2.setTotalQuestions(20);
        report2.setCorrectAnswers(18);
        report2.setScore(90.0);
        report2.setPercentage(90.0);
        reportDAO.save(report2);

        Report report3 = new Report();
        report3.setUserName("janedoe");
        report3.setExamId(3L);
        report3.setTotalQuestions(15);
        report3.setCorrectAnswers(10);
        report3.setScore(66.7);
        report3.setPercentage(66.7);
        reportDAO.save(report3);
    }

    @Test
    void testFindByUserName_Success() {
        // Test case where reports exist for the user "johndoe"
        List<Report> reports = reportDAO.findByUserName("johndoe");

        assertNotNull(reports);
        assertEquals(2, reports.size());
        assertTrue(reports.stream().allMatch(r -> r.getUserName().equals("johndoe")));
    }

    @Test
    void testFindByUserName_NoReportsFound() {
        // Test case where no reports exist for the user "nonexistentuser"
        List<Report> reports = reportDAO.findByUserName("nonexistentuser");

        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }

    @Test
    void testFindByUserNameWithLogging_Success() {
        // Testing the default method with logging for "johndoe"
        List<Report> reports = reportDAO.findByUserNameWithLogging("johndoe");

        assertNotNull(reports);
        assertEquals(2, reports.size());
        assertTrue(reports.stream().allMatch(r -> r.getUserName().equals("johndoe")));
    }

    @Test
    void testFindByUserNameWithLogging_NoReportsFound() {
        // Testing the default method with logging for a nonexistent user
        List<Report> reports = reportDAO.findByUserNameWithLogging("unknownuser");

        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }
}
