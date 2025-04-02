package com.academy.learning_journal_team3.service;

import com.academy.learning_journal_team3.entity.TeachingClass;
import com.academy.learning_journal_team3.entity.Topic;
import com.academy.learning_journal_team3.entity.TeachingClassTopic;
import com.academy.learning_journal_team3.entity.User;
import com.academy.learning_journal_team3.repository.TeachingClassRepository;
import com.academy.learning_journal_team3.repository.TeachingClassTopicRepository;
import com.academy.learning_journal_team3.repository.TopicsRepository;
import com.academy.learning_journal_team3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeachingClassTopicService {

    @Autowired
    private TeachingClassTopicRepository teachingClassTopicRepository;

    @Autowired
    private TeachingClassRepository teachingClassRepository;

    @Autowired
    private TopicsRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    public void addTeachingClassTopic(Long teachingClassId, Long topicId) {
        TeachingClass teachingClass = teachingClassRepository.findById(teachingClassId)
                .orElseThrow(() -> new RuntimeException("TeachingClass not found"));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        TeachingClassTopic teachingClassTopic = TeachingClassTopic.builder()
                .teachingClass(teachingClass)
                .topic(topic)
                .build();

        teachingClassTopicRepository.save(teachingClassTopic);
    }

    @Scheduled(cron = "0 0 0 * * 4L")
    @Transactional
    public void distributeTopicsLastThursdayOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.withDayOfMonth(1).minusDays(1);

        List<TeachingClass> teachingClasses = teachingClassRepository.findAll();
        for (TeachingClass teachingClass : teachingClasses) {
            List<User> usersInClass = teachingClass.getUserList();
            if (usersInClass.isEmpty()) continue;

            List<Topic> topicsAddedLastMonth = teachingClass.getTeachingClassTopics().stream()
                    .filter(teachingClassTopic -> {
                        LocalDateTime addedAt = teachingClassTopic.getAddedAt();
                        return addedAt != null &&
                                !addedAt.toLocalDate().isBefore(firstDayOfLastMonth) &&
                                !addedAt.toLocalDate().isAfter(lastDayOfLastMonth);
                    })
                    .map(TeachingClassTopic::getTopic)
                    .collect(Collectors.toList());

            if (topicsAddedLastMonth.isEmpty()) continue;

            usersInClass.forEach(user -> user.setAssignedTopic(null));

            Collections.shuffle(usersInClass);
            Collections.shuffle(topicsAddedLastMonth);

            for (int i = 0; i < usersInClass.size(); i++) {
                Topic assignedTopic = topicsAddedLastMonth.get(i % topicsAddedLastMonth.size());
                usersInClass.get(i).setAssignedTopic(assignedTopic);
            }

            userRepository.saveAll(usersInClass);
        }
    }
}

