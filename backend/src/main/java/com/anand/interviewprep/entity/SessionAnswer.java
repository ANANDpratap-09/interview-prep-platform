package com.anand.interviewprep.entity;

import jakarta.persistence.*;
import lombok.*;

/** One answered question within a PracticeSession. */
@Entity
@Table(name = "session_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private PracticeSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String givenAnswer;

    // For MCQ this is set automatically (string match). For SHORT_ANSWER,
    // there's no reliable auto-grading, so this stays null — the learner
    // self-reviews against the model answer instead (per FR11).
    private Boolean correct;
}