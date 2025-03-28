package com.cts.onlineexamportall.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.onlineexamportall.dao.ReportDAO;
import com.cts.onlineexamportall.exception.ReportNotFoundException;
import com.cts.onlineexamportall.model.Report;

public class ReportServiceTest {

    @Mock
    private ReportDAO reportDAO;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getReportsByUserName when reports exist
    @Test
    void testGetReportsByUserName_Success() {
        Report report1 = new Report();
        report1.setUserName("johndoe");
        report1.setExamId(1L);

        Report report2 = new Report();
        report2.setUserName("johndoe");
        report2.setExamId(2L);

        List<Report> mockReports = Arrays.asList(report1, report2);

        when(reportDAO.findByUserName("johndoe")).thenReturn(mockReports);

        List<Report> reports = reportService.getReportsByUserName("johndoe");

        assertNotNull(reports);
        assertEquals(2, reports.size());
        assertEquals("johndoe", reports.get(0).getUserName());
        assertEquals("johndoe", reports.get(1).getUserName());
        verify(reportDAO, times(1)).findByUserName("johndoe");
    }

    // Test for getReportsByUserName when no reports are found
    @Test
    void testGetReportsByUserName_ReportNotFound() {
        when(reportDAO.findByUserName("unknownUser")).thenReturn(Arrays.asList());

        Exception exception = assertThrows(ReportNotFoundException.class, () -> {
            reportService.getReportsByUserName("unknownUser");
        });

        assertEquals("Report for the username unknownUser not found", exception.getMessage());
        verify(reportDAO, times(1)).findByUserName("unknownUser");
    }
}

