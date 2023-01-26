package com.learn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    private String name;
    private String description;
    private String trainer;
    private String duration;
    private long maxRegistrations;
    private long noOfRegistrations;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name="course_relationship",joinColumns = @JoinColumn(name="course_name",referencedColumnName = "name"),inverseJoinColumns =
    @JoinColumn(name="user_id",referencedColumnName = "id"))
    private Set<User> users;

}
