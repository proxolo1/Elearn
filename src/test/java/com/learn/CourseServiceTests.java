package com.learn;

import com.learn.dto.AuthResponse;
import com.learn.dto.CourseDto;
import com.learn.exception.ResourceNotFoundException;
import com.learn.model.Course;
import com.learn.model.User;
import com.learn.repo.CourseRepository;
import com.learn.service.CourseService;
import com.learn.util.CourseDtoToCourseEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CourseServiceTests.class})
class CourseServiceTests {
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseService courseService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testGetAllCourses() {
        Course course1 = new Course();
        course1.setName("Math");
        Course course2 = new Course();
        course2.setName("History");
        List<Course> courses = Arrays.asList(course1, course2);

        when(courseRepository.findAll()).thenReturn(courses);

        ResponseEntity<List<Course>> response = courseService.getAllCourses();
        List<Course> result = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals("History", result.get(1).getName());
    }
    @Test
    void testAddCourse_Success() throws Exception {
        CourseDto newCourse = new CourseDto();
        newCourse.setName("Maths");
        newCourse.setDuration(new Date());
        newCourse.setDescription("something");
        newCourse.setMaxRegistrations(400);
        newCourse.setNoOfRegistrations(4);
        newCourse.setTrainer("john");
        // Set properties for newCourse
        when(courseRepository.existsByNameAndTrainer(any(), any())).thenReturn(false);
        when(courseRepository.save(any())).thenReturn(new Course());
        ResponseEntity<AuthResponse> response = courseService.addCourse(newCourse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isAccess());
        assertEquals(newCourse.getName() + " added successfully", response.getBody().getMessage());
    }
    @Test
    void testAddCourse_ResourceNotFoundException() throws Exception {
        CourseDto newCourse = new CourseDto();
        // Set properties for newCourse
        when(courseRepository.existsByNameAndTrainer(any(), any())).thenReturn(true);
        try {
            courseService.addCourse(newCourse);
        } catch (ResourceNotFoundException ex) {
            assertEquals(newCourse.getName() + " is already exist : ' already registered'", ex.getMessage());
        }
    }

    @Test
    void testAddCourse_IllegalArgumentException() throws Exception {
        try {
            CourseDto newCourse = new CourseDto();
            newCourse.setName("");
            newCourse.setDuration(new Date());
            newCourse.setDescription("something");
            newCourse.setMaxRegistrations(400);
            newCourse.setNoOfRegistrations(4);
            newCourse.setTrainer("john");
            courseService.addCourse(newCourse);
        } catch (IllegalArgumentException ex) {
            assertEquals("new course fields cannot be empty", ex.getMessage());
        }
    }
    @Test
    void updateCourseSuccessfully() throws InvocationTargetException, IllegalAccessException {
        // given
        long id = 1L;
        CourseDto updatedCourse = new CourseDto(1,"MATHS","description","trainer",new Date(),100,39);
        Course existingCourse = new Course(1,"Test","test","test",new Date(),100,39,null);
        when(courseRepository.findById(id)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        // when
        ResponseEntity<AuthResponse> response = courseService.updateCourse(id, updatedCourse);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse.getName()+" updated successfully", response.getBody().getMessage());

        verify(courseRepository, times(1)).findById(id);
        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    void updateCourseWithBlankFields() {
        // given
        long id = 1L;
        CourseDto updatedCourse = new CourseDto();

        // when
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> courseService.updateCourse(id, updatedCourse));



        verify(courseRepository, never()).findById(id);
        verify(courseRepository, never()).save(any(Course.class));
    }
    @Test
     void deleteCourseTest() {
        long id = 1L;
        Course courseToDelete = new Course();
        courseToDelete.setName("Java 101");
        courseToDelete.setId(id);
        when(courseRepository.findById(id)).thenReturn(Optional.of(courseToDelete));
        AuthResponse response = courseService.deleteCourse(id).getBody();
        assertEquals("Java 101 deleted successfully", response.getMessage());
        verify(courseRepository, times(1)).delete(courseToDelete);
    }
    @Test
    void getCourseByName_validName_returnsCourse() {
        Course expectedCourse = new Course();
        expectedCourse.setName("Java");

        when(courseRepository.findByName(anyString())).thenReturn(java.util.Optional.of(expectedCourse));

        ResponseEntity<Course> actualResponse = courseService.getCourseByName("Java");

        assertEquals(expectedCourse, actualResponse.getBody());
        assertEquals(200, actualResponse.getStatusCodeValue());
    }
    @Test
    void getCourseByName_invalidName_throwsException() {
        when(courseRepository.findByName(anyString())).thenReturn(java.util.Optional.empty());

        try {
            courseService.getCourseByName("Invalid Name");
        } catch (ResourceNotFoundException ex) {
            assertEquals("COURSE not found with Invalid Name : 'not exist'", ex.getMessage());
        }
    }

//    @Test
//    void getCourseByName_emptyName_throwsException() {
//        try {
//            courseService.getCourseByName("");
//        } catch (IllegalArgumentException ex) {
//            assertEquals("course empty is empty", ex.getMessage());
//        }
//    }

}

