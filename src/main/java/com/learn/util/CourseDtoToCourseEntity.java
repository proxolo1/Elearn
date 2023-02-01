package com.learn.util;

import com.learn.dto.CourseDto;
import com.learn.model.Course;

public class CourseDtoToCourseEntity {
     CourseDtoToCourseEntity() {
        throw new IllegalStateException("Utility class");
    }

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
