package com.academy.learning_journal_team3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachingclass")
public class TeachingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // TeachingClass Entity
    @OneToMany(mappedBy = "teachingClass")
    private List<User> userList;

    @OneToMany(mappedBy = "teachingClass", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeachingClassTopic> teachingClassTopics;
}
