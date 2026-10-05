package com.anand.interviewprep.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A course is the top-level piece of content — e.g. "Java Fundamentals".
 * It owns an ordered list of Modules via the `modules` field below.
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String topic; // e.g. "Java", "DBMS", "DSA"

    @Column(nullable = false)
    private String difficulty; // "BEGINNER", "INTERMEDIATE", "ADVANCED"

    // true = visible to learners, false = draft/archived (admin-only visibility)
    @Column(nullable = false)
    @Builder.Default
    private boolean published = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * mappedBy = "course" means Module owns the foreign key (course_id column).
     * cascade = ALL: saving/deleting a Course saves/deletes its Modules too.
     * orphanRemoval = true: removing a Module from this list deletes it from the DB.
     * We initialize the list so it's never null when building a new Course.
     */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Module> modules = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}