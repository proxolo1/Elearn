package com.learn.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String trainer;
    private Date duration;
    private long maxRegistrations;
    private long noOfRegistrations;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="course_relationship",joinColumns = @JoinColumn(name="course_id",referencedColumnName = "id"),inverseJoinColumns =
    @JoinColumn(name="user_id",referencedColumnName = "id"))
    private Set<User> users;



}
