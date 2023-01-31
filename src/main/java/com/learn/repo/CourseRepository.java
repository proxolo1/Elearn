package com.learn.repo;

import com.learn.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Boolean existsByName(String courseName);
    Boolean existsByTrainer(String trainer);
    Optional<Course>findByName(String courseName);

    boolean existsByNameAndTrainer(String name, String trainer);
}
