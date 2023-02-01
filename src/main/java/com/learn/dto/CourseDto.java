package com.learn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private long id;
    private String name;
    private String description;
    private String trainer;
    private Date duration;
    private long maxRegistrations;
    private long noOfRegistrations;
}
