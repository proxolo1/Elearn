package com.learn.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor

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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Course course = (Course) o;
        return name != null && Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
