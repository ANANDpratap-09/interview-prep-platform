package com.anand.interviewprep.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * One question in the practice bank. `type` distinguishes MCQ (checked
 * automatically against correctAnswer) from SHORT_ANSWER (shown with a
 * model answer for the learner to self-grade — see FR11 in the SRS).
 * `options` is only used for MCQ; stored as a single delimited string to
 * keep the schema simple instead of a separate options table.
 */
@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic; // "Java", "DBMS", "OS", "DSA", etc.

    @Column(nullable = false)
    private String difficulty; // "EASY", "MEDIUM", "HARD"

    @Column(nullable = false)
    private String type; // "MCQ" or "SHORT_ANSWER"

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String body; // the question text

    // For MCQ: options separated by "|", e.g. "4|8|12|16"
    // For SHORT_ANSWER: left null.
    @Column(length = 1000)
    private String options;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String correctAnswer; // the correct option text (MCQ) or model answer (SHORT_ANSWER)

    @Lob
    @Column(columnDefinition = "TEXT")
    private String explanation; // shown after answering, for either type
}