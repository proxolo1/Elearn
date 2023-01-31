package com.learn.service;

import com.learn.dto.AuthResponse;
import com.learn.exception.ResourceNotFoundException;
import com.learn.model.Course;
import com.learn.model.User;
import com.learn.repo.CourseRepository;
import com.learn.repo.UserRepository;
import com.learn.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class CourseService {
    static final String COURSE="COURSE";
    final Logger logger = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<Course>> getAllCourses() {
        logger.info("Getting all courses");
        return ResponseEntity.ok(courseRepository.findAll());
    }

    public ResponseEntity<AuthResponse> addCourse(Course newCourse) throws InvocationTargetException, IllegalAccessException {
        if (ValidationUtil.isBlank(newCourse)) {
            logger.error("new course fields are empty");
            throw new IllegalArgumentException("new course fields cannot be empty");
        }
        if (courseRepository.existsByNameAndTrainer(newCourse.getName(), newCourse.getTrainer())) {
            throw new ResourceNotFoundException(newCourse.getName(), " already registered");
        }
        logger.info("Adding new course");
        courseRepository.save(newCourse);
        return ResponseEntity.ok(new AuthResponse(newCourse.getName() + " added successfully", true));
    }

    public ResponseEntity<AuthResponse> updateCourse(long id, Course updatedCourse) throws InvocationTargetException, IllegalAccessException {
        if (ValidationUtil.isBlank(updatedCourse) &&ValidationUtil.isBlank(id)) {
            logger.error("updateCourse id or course fields is empty");
            throw new IllegalArgumentException("id or course is have empty fields");
        }
        Course existingCourse = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COURSE, String.valueOf(id), "course not exist"));
        existingCourse.setName(updatedCourse.getName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setDuration(updatedCourse.getDuration());
        existingCourse.setNoOfRegistrations(updatedCourse.getNoOfRegistrations());
        existingCourse.setMaxRegistrations(updatedCourse.getMaxRegistrations());
        courseRepository.save(existingCourse);
        logger.info("Updating course");
        return ResponseEntity.ok(new AuthResponse(existingCourse.getName() + " updated successfully", true));
    }

    public ResponseEntity<AuthResponse> deleteCourse(long id) {
        if (ValidationUtil.isBlank(id)) {
            logger.error("course id is empty");
            throw new IllegalArgumentException("course id should not be empty");
        }
        Course courseToDelete = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COURSE, String.valueOf(id), "not exist"));
        logger.info("Deleting course");
        courseRepository.delete(courseToDelete);
        return ResponseEntity.ok(new AuthResponse(courseToDelete.getName() + " deleted successfully", true));
    }

    public ResponseEntity<Course> getCourseByName(String courseName) {
        if(ValidationUtil.isBlank(courseName)){
            logger.error("course name is empty");
            throw new IllegalArgumentException("course empty is empty");
        }
        Course course = courseRepository.findByName(courseName).orElseThrow(() -> new ResourceNotFoundException(COURSE, courseName, "not exist"));
        return ResponseEntity.ok(course);
    }

    public String enrollCourse(String email, Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (course.getUsers().contains(user)) {
            return user.getFirstName() + " already enrolled " + course.getName();
        }
        course.setNoOfRegistrations(course.getNoOfRegistrations() + 1);

        course.getUsers().add(user);
        courseRepository.save(course);
        return user.getEmail() + " successfully enrolled";
    }
}
