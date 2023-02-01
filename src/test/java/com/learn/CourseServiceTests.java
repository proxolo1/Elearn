package com.learn;

import com.learn.dto.AuthResponse;
import com.learn.dto.CourseDto;
import com.learn.model.Course;
import com.learn.model.User;
import com.learn.repo.CourseRepository;
import com.learn.service.CourseService;
import com.learn.util.CourseDtoToCourseEntity;
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
        courses.add(new Course(1,"test", "test", "test", new Date(), 2000, 10, users));
        courses.add(new Course(1,"android", "android studio", "ajay", new Date(), 200000, 1000, users));
        when(courseRepository.findAll()).thenReturn(courses);
        assertEquals(2, Objects.requireNonNull(courseService.getAllCourses().getBody()).size());
        assertEquals(courses,courseService.getAllCourses().getBody());
    }

    @Test
    void test_addCourse() throws InvocationTargetException, IllegalAccessException {
        CourseDto course = new CourseDto(1,"test", "test", "test", new Date(), 2000, 10);
        when(courseRepository.save(CourseDtoToCourseEntity.convert(course))).thenReturn(null);
        assertEquals(ResponseEntity.ok(new AuthResponse(course.getName() + " added successfully", true)), courseService.addCourse(course));
    }

    @Test
    void test_updateCourse() throws InvocationTargetException, IllegalAccessException {
        Long id = 1L;
        Optional<Course> oldCourse = Optional.of(new Course(1,"test", "test", "test", new Date(), 2000, 10, users));
        CourseDto updatedCourse = new CourseDto(1,"test", "android developing", "ajay", new Date(), 10, 20);
        when(courseRepository.findById(id)).thenReturn(oldCourse);
        when(courseRepository.save(oldCourse.get())).thenReturn(null);
        assertEquals(ResponseEntity.ok(new AuthResponse(String.format("%s updated successfully", updatedCourse.getName()), true)), courseService.updateCourse(id, updatedCourse));

    }

    @Test
    void test_deleteCourse() {
        Optional<Course> course = Optional.of(new Course(1,"test", "test", "test", new Date(), 2000, 10, users));
        when(courseRepository.findById(course.get().getId())).thenReturn(course);
        doNothing().when(courseRepository).delete(course.get());
        assertEquals(ResponseEntity.ok(new AuthResponse(String.format("%s deleted successfully", course.get().getName()), true)),courseService.deleteCourse(course.get().getId()));
        verify(courseRepository,times(1)).delete(course.get());
    }
}
