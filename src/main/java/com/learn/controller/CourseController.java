package com.learn.controller;

import com.learn.dto.AuthResponse;
import com.learn.dto.CourseDto;
import com.learn.model.Course;
import com.learn.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin()
public class CourseController {
    /**
     * Autowired instance of {@link CourseService} to handle the course related operations.
     */
    @Autowired
    private CourseService courseService;

    /**
     * Logger instance to log the information of the requests received.
     */
    final Logger logger = LoggerFactory.getLogger(CourseController.class);

    /**
     * Endpoint to handle the request for retrieving all the available courses.
     *
     * @return A ResponseEntity object containing the list of all the courses.
     */
    @GetMapping("course")
    public ResponseEntity<List<Course>> allCourses() {
        logger.info("Received a request to the endpoint '/GET Course'");
        return courseService.getAllCourses();
    }

    /**
     * Endpoint to handle the request for retrieving a course by its name.
     *
     * @param courseName The name of the course to be retrieved.
     * @return A ResponseEntity object containing the course with the given name.
     */
    @GetMapping("course/{courseName}")
    public ResponseEntity<Course> getCourseByName(@PathVariable String courseName) {
        logger.info("Received a request to the endpoint '/GET {}'", courseName);
        return courseService.getCourseByName(courseName);
    }

    /**
     * Endpoint to handle the request for enrolling a course by its id and email.
     *
     * @param id    The id of the course to be enrolled.
     * @param email The email of the student to be enrolled.
     * @return A ResponseEntity object containing the enrollment status.
     */
    @GetMapping("enroll-course")
    public ResponseEntity<AuthResponse> enrollCourse(@RequestParam("id") Long id, @RequestParam("email") String email) {
        return courseService.enrollCourse(email, id);
    }

    /**
     * Endpoint to handle the request for adding a new course.
     * This endpoint can be accessed only by the user with ROLE_ADMIN.
     *
     * @param course The course object to be added.
     * @return A ResponseEntity object containing the addition status.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("course")
    public ResponseEntity<AuthResponse> addCourse(@RequestBody CourseDto course) throws InvocationTargetException, IllegalAccessException {
        logger.info("Received a request to the endpoint '/POST {}'", course);
        return courseService.addCourse(course);
    }
    /**

     This endpoint updates a course with the given ID by updating it with the details in the request body.
     @param id The ID of the course to be updated.
     @param newCourse The updated details of the course in the form of a {@link CourseDto} object.
     @return A {@link ResponseEntity} object containing the result of the update operation.
     @throws IllegalAccessException When the updates to the course object cannot be performed due to access restrictions.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("course/{id}")
    public ResponseEntity<AuthResponse> updateCourse(@PathVariable Long id, @RequestBody CourseDto newCourse) throws InvocationTargetException, IllegalAccessException {
        logger.info("Received a request to the endpoint '/PUT with id :{} and Course :{}'", id, newCourse);
        return courseService.updateCourse(id, newCourse);
    }
    /**

     This endpoint deletes a course with the given ID.
     @param id The ID of the course to be deleted.
     @return A {@link ResponseEntity} object containing the result of the delete operation.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("course/{id}")
    public ResponseEntity<AuthResponse> deleteCourse(@PathVariable Long id) {
        logger.info("Received a request to the endpoint '/DELETE with id {}'", id);
        return courseService.deleteCourse(id);
    }

}
