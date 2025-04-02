package com.academy.learning_journal_team3.repository;

import com.academy.learning_journal_team3.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicsRepository extends JpaRepository<Topic, Long> {
    Topic findByName(String name);
}
