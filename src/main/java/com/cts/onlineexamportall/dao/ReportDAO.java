package com.cts.onlineexamportall.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.onlineexamportall.model.Report;

@Repository
public interface ReportDAO extends JpaRepository<Report, Long> {

    Logger logger = LogManager.getLogger(ReportDAO.class);

    List<Report> findByUserName(String username);

    default List<Report> findByUserNameWithLogging(String username) {
        logger.info("Finding reports by username: {}", username);
        List<Report> reports = findByUserName(username);
        if (reports.isEmpty()) {
            logger.warn("No reports found for username: {}", username);
        } else {
            logger.info("Reports found: {}", reports.size());
        }
        return reports;
    }
}