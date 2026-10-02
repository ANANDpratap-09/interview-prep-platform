package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Spring Data JPA generates the implementation of this interface for us at
 * runtime. We never write SQL here — method names like findByEmail are
 * parsed by Spring into the matching query automatically.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}