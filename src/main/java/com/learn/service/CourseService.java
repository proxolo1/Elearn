package com.learn.service;

import com.learn.dto.AuthResponse;
import com.learn.dto.CourseDto;
import com.learn.exception.ResourceNotFoundException;
import com.learn.model.Course;
import com.learn.model.User;
import com.learn.repo.CourseRepository;
import com.learn.repo.UserRepository;
import com.learn.util.CourseDtoToCourseEntity;
import com.learn.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
/**
 * CourseService is a class that provides various course related services like
 * getting all courses, adding new course, updating course, deleting course,
 * getting course by name, and enrolling in a course.
 */
@Service
public class CourseService {
    static final String COURSE="COURSE";
    final Logger logger = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    /**
     * This method retrieves all courses from the database and returns them in a list.
     *
     * @return A ResponseEntity containing the list of all courses.
     */
    public ResponseEntity<List<Course>> getAllCourses() {
        logger.info("Getting all courses");
        return ResponseEntity.ok(courseRepository.findAll());
    }
    /**
     * This method adds a new course to the database.
     *
     * @param newCourse A CourseDto object containing the details of the new course.
     * @return A ResponseEntity containing the result of the operation.
     */
    public ResponseEntity<AuthResponse> addCourse(CourseDto newCourse) throws InvocationTargetException, IllegalAccessException {
        if (courseRepository.existsByNameAndTrainer(newCourse.getName(), newCourse.getTrainer())) {
            throw new ResourceNotFoundException(newCourse.getName(), " already registered");
        }
        if (ValidationUtil.isBlank(newCourse)) {
            logger.error("new course fields are empty");
            throw new IllegalArgumentException("new course fields cannot be empty");
        }
        logger.info("Adding new course");
        courseRepository.save(CourseDtoToCourseEntity.convert(newCourse));
        return ResponseEntity.ok(new AuthResponse(newCourse.getName() + " added successfully", true));
    }
    /**
     * This method updates the details of an existing course in the database.
     *
     * @param id The id of the course to be updated.
     * @param updatedCourse A CourseDto object containing the updated details.
     * @return A ResponseEntity containing the result of the operation.

     */
    public ResponseEntity<AuthResponse> updateCourse(long id, CourseDto updatedCourse) throws InvocationTargetException, IllegalAccessException {
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
        existingCourse.setTrainer(updatedCourse.getTrainer());
        courseRepository.save(existingCourse);
        logger.info("Updating course");
        return ResponseEntity.ok(new AuthResponse(existingCourse.getName() + " updated successfully", true));
    }
    /**
     * This method deletes a course from the database.
     *
     * @param id The id of the course to be deleted.
     * @return A ResponseEntity containing the result of the operation.
     */
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
    /**
     * This method retrieves a course from the database by its name.
     *
     * @param courseName The name of the course to be retrieved.
     * @return A ResponseEntity containing the retrieved course.
     */
    public ResponseEntity<Course> getCourseByName(String courseName) {
        if(ValidationUtil.isBlank(courseName)){
            logger.error("course name is empty");
            throw new IllegalArgumentException("course empty is empty");
        }
        Course course = courseRepository.findByName(courseName).orElseThrow(() -> new ResourceNotFoundException(COURSE, courseName, "not exist"));
        return ResponseEntity.ok(course);
    }
    /**
     * This method enrolls a user in a course.
     *
     * @param email The email of the user to be enrolled.
     * @param id The id of the course.
     * @return A ResponseEntity containing the result of the operation.
     */
    public ResponseEntity<AuthResponse> enrollCourse(String email, Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COURSE, id.toString(), "not exist"));
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User",email,"not exist"));

        if (course.getUsers().contains(user)) {
            return ResponseEntity.ok(new AuthResponse(user.getFirstName() + " already enrolled " + course.getName(),true));
        }
        if(course.getMaxRegistrations()<course.getNoOfRegistrations()){
            return ResponseEntity.ok(new AuthResponse("course registration limit exceed",false));
        }
        course.setNoOfRegistrations(course.getNoOfRegistrations() + 1);

        course.getUsers().add(user);
        courseRepository.save(course);
        return ResponseEntity.ok(new AuthResponse(user.getEmail() + " successfully enrolled",true));
    }

}
