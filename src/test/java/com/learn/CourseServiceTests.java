package com.learn;

import com.learn.dto.AuthResponse;
import com.learn.model.Course;
import com.learn.model.User;
import com.learn.repo.CourseRepository;
import com.learn.service.CourseService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CourseServiceTests.class})
class CourseServiceTests {
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseService courseService;
    public List<Course> courses = new ArrayList<>();
    public Set<User> users;

    @Test
    @Order(1)
    void test_getCourseList() {
        courses.add(new Course(1,"test", "test", "test", "test", 2000, 10, users));
        courses.add(new Course(1,"android", "android studio", "ajay", "30 months", 200000, 1000, users));
        when(courseRepository.findAll()).thenReturn(courses);
        assertEquals(2, Objects.requireNonNull(courseService.getAllCourses().getBody()).size());
        assertEquals(courses,courseService.getAllCourses().getBody());
    }

    @Test
    void test_addCourse() throws InvocationTargetException, IllegalAccessException {
        Course course = new Course(1,"test", "test", "test", "test", 2000, 10, users);
        when(courseRepository.save(course)).thenReturn(null);
        assertEquals(ResponseEntity.ok(new AuthResponse(course.getName() + " added successfully", true)), courseService.addCourse(course));
    }

    @Test
    void test_updateCourse() throws InvocationTargetException, IllegalAccessException {
        Long id = 1L;
        Optional<Course> oldCourse = Optional.of(new Course(1,"test", "test", "test", "test", 2000, 10, users));
        Course updatedCourse = new Course(1,"test", "android developing", "ajay", "2 months", 10, 20, users);
        when(courseRepository.findById(id)).thenReturn(oldCourse);
        when(courseRepository.save(oldCourse.get())).thenReturn(null);
        assertEquals(ResponseEntity.ok(new AuthResponse(String.format("%s is updated", id), true)), courseService.updateCourse(id, updatedCourse));

    }

    @Test
    void test_deleteCourse() {
        Optional<Course> course = Optional.of(new Course(1,"test", "test", "test", "test", 2000, 10, users));
        when(courseRepository.findById(course.get().getId())).thenReturn(course);
        doNothing().when(courseRepository).delete(course.get());
        assertEquals(ResponseEntity.ok(new AuthResponse(String.format("%s is deleted", course.get().getName()), true)),courseService.deleteCourse(course.get().getId()));
        verify(courseRepository,times(1)).delete(course.get());
    }
}
