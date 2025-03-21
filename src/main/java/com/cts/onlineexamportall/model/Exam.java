package com.cts.onlineexamportall.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long examId;

    private String title;
    private String description;
    private int totalMarks;
    private int duration;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

}
