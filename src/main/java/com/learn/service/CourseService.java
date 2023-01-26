package com.learn.service;

import com.learn.dto.AuthResponse;
import com.learn.exception.ResourceNotFoundException;
import com.learn.model.Course;
import com.learn.model.User;
import com.learn.repo.CourseRepository;
import com.learn.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    final Logger logger = LoggerFactory.getLogger(CourseService.class);
    public ResponseEntity<List<Course>> getCourseList() {
        logger.info("all course list");
        return ResponseEntity.ok(courseRepository.findAll()) ;
    }

    public ResponseEntity<AuthResponse> addCourse(Course course) {
        try{
            courseRepository.save(course);
            logger.info("added course");
            courseRepository.save(course);
            return ResponseEntity.ok(new AuthResponse(course.getName()+" added successfully",true));
        }
        catch(Exception e){
            throw new ResourceNotFoundException("course",e.getMessage(),"creating new course error");
        }


        }

    public ResponseEntity<AuthResponse> updateCourse(String courseName, Course updatedCourse) {
        Course oldCourse = courseRepository.findById(courseName).orElseThrow(()->new ResourceNotFoundException("Course",courseName,"not exist"));
        oldCourse.setName(updatedCourse.getName());
        oldCourse.setDescription(updatedCourse.getDescription());
        oldCourse.setDuration(updatedCourse.getDuration());
        oldCourse.setNoOfRegistrations(updatedCourse.getNoOfRegistrations());
        oldCourse.setMaxRegistrations(updatedCourse.getMaxRegistrations());
        courseRepository.save(oldCourse);
        logger.info("updated course");
        return ResponseEntity.ok(new AuthResponse(String.format("%s is updated",courseName),true));
    }

    public ResponseEntity<AuthResponse> deleteCourse(String courseName) {
        Course course=courseRepository.findById(courseName).orElseThrow(()->new ResourceNotFoundException("Course",courseName,"not exist"));
        logger.info("deleted course");
        courseRepository.delete(course);
        return ResponseEntity.ok(new AuthResponse(String.format("%s is deleted",courseName),true));
    }

    public String enrollCourse(String email,String courseName) {
        Course course=courseRepository.findById(courseName).orElseThrow();
        User user=userRepository.findByEmail(email).orElseThrow();

        if(course.getUsers().contains(user)){
            return user.getFirstName()+" already enrolled "+course.getName();
        }
        course.setNoOfRegistrations(course.getNoOfRegistrations()+1);

        course.getUsers().add(user);
        courseRepository.save(course);
        return user.getEmail()+ " successfully enrolled";
    }

    public Course getCourse(String courseName) {
        return courseRepository.findById(courseName).orElseThrow();
    }
}
