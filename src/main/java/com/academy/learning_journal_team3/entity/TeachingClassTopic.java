package com.academy.learning_journal_team3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teaching_class_topic", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teaching_class_id", "topic_id"})
})
public class TeachingClassTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TeachingClassTopic Entity
    @ManyToOne
    @JoinColumn(name = "teaching_class_id", nullable = false)
    private TeachingClass teachingClass;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDateTime.now();
    }
}
