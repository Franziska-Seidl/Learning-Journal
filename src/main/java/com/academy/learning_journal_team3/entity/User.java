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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role; // ADMIN, USER

    @OneToMany(mappedBy = "user")
    private List<Entry> entryList;

    // User Entity
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private TeachingClass teachingClass;

    @ManyToOne
    @JoinColumn(name = "assigned_topic_id")
    private Topic assignedTopic;
}
