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
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // Topic Entity
    @OneToMany(mappedBy = "topic")
    private List<Entry> entryList;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TeachingClassTopic> teachingClassTopics;
}
