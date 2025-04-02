package com.academy.learning_journal_team3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "entries")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @Column
    private String title;

    @Column
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name="topic_id")
    private Topic topic;
}
