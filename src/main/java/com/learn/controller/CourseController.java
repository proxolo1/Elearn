package com.learn.controller;

import com.learn.dto.AuthResponse;
import com.learn.dto.CourseDto;
import com.learn.model.Course;
import com.learn.model.User;
import com.learn.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api")
public class CourseController {
    @Autowired
    private CourseService courseService;
    final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @GetMapping("course")
    public ResponseEntity<List<Course>> allCourses() {
        logger.info("Received a request to the endpoint '/GET Course'");
        return courseService.getAllCourses();
    }

    @GetMapping("course/{courseName}")
    public ResponseEntity<Course> getCourseByName(@PathVariable String courseName) {
        logger.info("Received a request to the endpoint '/GET {}'",courseName);
        return courseService.getCourseByName(courseName);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("course")
    public ResponseEntity<AuthResponse> addCourse(@RequestBody CourseDto course) throws InvocationTargetException, IllegalAccessException {
        logger.info("Received a request to the endpoint '/POST {}'",course);
        return courseService.addCourse(course);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("course/{id}")
    public ResponseEntity<AuthResponse> updateCourse(@PathVariable Long id, @RequestBody CourseDto newCourse) throws InvocationTargetException, IllegalAccessException {
        logger.info("Received a request to the endpoint '/PUT with id :{} and Course :{}'",id,newCourse);
        return courseService.updateCourse(id, newCourse);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("course/{id}")
    public ResponseEntity<AuthResponse> deleteCourse(@PathVariable Long id) {
        logger.info("Received a request to the endpoint '/DELETE with id {}'",id);
        return courseService.deleteCourse(id);
    }
    @GetMapping("enroll-course")
    public ResponseEntity<AuthResponse> enrollCourse(@RequestParam("id") Long id,@RequestParam("email") String email){
        return courseService.enrollCourse(email,id);
    }
    @GetMapping("/*")
    public ModelAndView Home(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("./show.html");
        return modelAndView;
    }

}
