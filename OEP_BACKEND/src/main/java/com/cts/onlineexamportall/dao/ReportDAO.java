package com.cts.onlineexamportall.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.cts.onlineexamportall.model.Report;

@Repository
public interface ReportDAO extends JpaRepository<Report, Long> {

    List<Report> findByUserName(String username);

    Report findByUserNameAndExamId(String userName, long examId);



}