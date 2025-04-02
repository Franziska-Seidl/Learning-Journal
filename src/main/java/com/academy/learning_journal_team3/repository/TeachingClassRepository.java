package com.academy.learning_journal_team3.repository;

import com.academy.learning_journal_team3.entity.TeachingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingClassRepository extends JpaRepository<TeachingClass, Long> {

}
