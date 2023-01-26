package com.learn.controller;

import com.learn.dto.AuthResponse;
import com.learn.model.Course;
import com.learn.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class CourseController {
    @Autowired
    private CourseService courseService;
@GetMapping("all-courses")
    public ResponseEntity<List<Course>> allCourses(){
    return courseService.getCourseList();
}
@PostMapping("add-course")
    public ResponseEntity<AuthResponse> addCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }
    @PutMapping("update-course/{courseName}")
    public ResponseEntity<AuthResponse>updateCourse(@PathVariable String courseName,@RequestBody Course updatedCourse){
    return courseService.updateCourse(courseName,updatedCourse);
    }
    @DeleteMapping("delete-course/{courseName}")
    public ResponseEntity<AuthResponse>deleteCourse(@PathVariable String courseName){
    return courseService.deleteCourse(courseName);
    }
}
