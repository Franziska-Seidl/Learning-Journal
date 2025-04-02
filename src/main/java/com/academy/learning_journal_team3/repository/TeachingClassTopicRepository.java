package com.academy.learning_journal_team3.repository;

import com.academy.learning_journal_team3.entity.TeachingClassTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachingClassTopicRepository extends JpaRepository<TeachingClassTopic, Long> {
}