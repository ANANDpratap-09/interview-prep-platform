package com.anand.interviewprep.entity;

/**
 * The two kinds of account in the system.
 * LEARNER: default role for anyone who registers.
 * ADMIN: manually promoted in the database, can manage courses and questions.
 */
public enum Role {
    LEARNER,
    ADMIN
}