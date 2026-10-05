package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
}