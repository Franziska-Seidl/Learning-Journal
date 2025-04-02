package com.academy.learning_journal_team3.repository;

import com.academy.learning_journal_team3.entity.Entry;


import com.academy.learning_journal_team3.entity.Topic;
import com.academy.learning_journal_team3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByUser(User user);

    List<Entry> findByTopic(Topic topic);
}
