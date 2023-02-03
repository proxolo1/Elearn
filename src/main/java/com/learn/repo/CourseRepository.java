package com.learn.repo;

import com.learn.model.Course;
import com.learn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Optional<Course>findByName(String courseName);
    boolean existsByNameAndTrainer(String name, String trainer);
}
