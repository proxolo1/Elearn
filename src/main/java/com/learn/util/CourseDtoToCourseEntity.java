package com.learn.util;

import com.learn.dto.CourseDto;
import com.learn.model.Course;
/**
 * Utility class to convert a `CourseDto` object to a `Course` entity object.
 *
 * <p>
 * This class is not meant to be instantiated as it only contains a single static method for conversion.
 * Hence, the constructor has been marked as private and throws an {@link IllegalStateException} if called.
 * </p>
 *
 * @see CourseDto
 * @see Course
 */
public class CourseDtoToCourseEntity {
    /**
     * Private constructor to prevent instantiation of the class.
     *
     * @throws IllegalStateException always thrown when this constructor is called.
     */
     CourseDtoToCourseEntity() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Converts a given `CourseDto` object to a `Course` entity object.
     *
     * @param courseDto the `CourseDto` object to be converted.
     * @return the converted `Course` entity object.
     */
    public static Course convert(CourseDto courseDto){
        Course course=new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setMaxRegistrations(courseDto.getMaxRegistrations());
        course.setTrainer(courseDto.getTrainer());
        course.setDuration(courseDto.getDuration());
        return course;
    }
}
