package com.anand.interviewprep.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Maps directly to the `users` table. Hibernate creates/updates this table
 * for us because of spring.jpa.hibernate.ddl-auto=update.
 *
 * Lombok annotations below generate boilerplate at compile time:
 * @Getter/@Setter  -> getName(), setName(), etc. for every field
 * @NoArgsConstructor -> a no-args constructor (JPA requires this)
 * @AllArgsConstructor -> a constructor taking every field (handy for tests)
 * @Builder -> lets us write User.builder().name("x").email("y").build()
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // unique = true adds a UNIQUE constraint at the DB level, so two users
    // can never share an email even under concurrent requests.
    @Column(nullable = false, unique = true)
    private String email;

    // We NEVER store the raw password. This holds a BCrypt hash produced
    // by PasswordEncoder in AuthService.
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING) // stores "LEARNER" / "ADMIN" as text, not 0/1
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Runs automatically right before the first INSERT.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}