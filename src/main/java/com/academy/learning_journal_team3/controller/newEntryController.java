package com.academy.learning_journal_team3.controller;

import com.academy.learning_journal_team3.entity.Entry;
import com.academy.learning_journal_team3.entity.Topic;
import com.academy.learning_journal_team3.entity.User;
import com.academy.learning_journal_team3.repository.EntryRepository;
import com.academy.learning_journal_team3.repository.TopicsRepository;
import com.academy.learning_journal_team3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class newEntryController {
    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicsRepository topicsRepository;

    @PostMapping("/newEntry")
    public String createOrUpdateNewEntry(
            Authentication authentication,
            @RequestParam(name="Entry") String formText,
            @RequestParam(name="Title") String formTitle,
            @RequestParam(name="Topics") Long formTopicId,
            @RequestParam(name="EntryId", required=false) Long formEntryId
            ){
        User user  = userService.findByEmail(authentication.getName());
        Topic topic = topicsRepository.findById(formTopicId).get();

        if(formEntryId != null){
            Entry updateEntry = entryRepository.findById(formEntryId).get();
            updateEntry.setTitle(formTitle);
            updateEntry.setTopic(topic);
            updateEntry.setContent(formText);
            entryRepository.save(updateEntry);
        }else {
            Entry newEntry = Entry.builder().content(formText).title(formTitle).user(user).topic(topic).build();
            entryRepository.save(newEntry);
        }
        return "redirect:/entries";
    }
}
