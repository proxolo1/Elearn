package com.learn.repo;

import com.learn.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,String> {
    Boolean existsByName(String name);
}
