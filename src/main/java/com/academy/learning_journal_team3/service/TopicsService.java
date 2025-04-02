package com.academy.learning_journal_team3.service;

import com.academy.learning_journal_team3.entity.TeachingClass;
import com.academy.learning_journal_team3.entity.TeachingClassTopic;
import com.academy.learning_journal_team3.entity.Topic;
import com.academy.learning_journal_team3.dto.TopicsModel;
import com.academy.learning_journal_team3.repository.TeachingClassRepository;
import com.academy.learning_journal_team3.repository.TopicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TopicsService {
    @Autowired
    private TopicsRepository topicsRepository;

    @Autowired
    private TeachingClassRepository teachingClassRepository;

    public List<Topic> getAllTopics () {
        return topicsRepository.findAll();
    }

    public Long createTopic (TopicsModel topicsModel){
        System.out.println(topicsModel.name());
        for (Topic currentTopic : topicsRepository.findAll()) {
            if (currentTopic.getName().equals(topicsModel.name())) {
                throw new RuntimeException("Thema existiert bereits");
            }
        }
        Topic topic = topicsRepository.save(Topic.builder()
                .name(topicsModel.name())
                .build());
        return topic.getId();
    }

    public List<Topic> getTopicsNotInClass(Long classId) {
        TeachingClass teachingClass = teachingClassRepository.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Klassenzimmer mit ID " + classId + " nicht gefunden"));

        List<TeachingClassTopic> classTopics = teachingClass.getTeachingClassTopics();

//        return topicsRepository.findAll().stream()
//                .filter(topic -> !classTopics.contains(topic))
//                .collect(Collectors.toList());

        return topicsRepository.findAll().stream()
                .filter(topic -> classTopics.stream()
                        .noneMatch(classTopic -> classTopic.getTopic().equals(topic)))
                .collect(Collectors.toList());
    }

    public List<Topic> getTopicsInClass(Long classId) {
        TeachingClass teachingClass = teachingClassRepository.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Klassenzimmer mit ID " + classId + " nicht gefunden"));

        List<TeachingClassTopic> classTopics = teachingClass.getTeachingClassTopics();

//        return topicsRepository.findAll().stream()
//                .filter(topic -> classTopics.contains(topic))
//                .collect(Collectors.toList());

        return topicsRepository.findAll().stream()
                .filter(topic -> classTopics.stream()
                        .anyMatch(classTopic -> classTopic.getTopic().equals(topic)))
                .collect(Collectors.toList());
    }
}
