package com.academy.learning_journal_team3.controller;
import com.academy.learning_journal_team3.entity.Entry;
import com.academy.learning_journal_team3.entity.Topic;
import com.academy.learning_journal_team3.entity.User;
import com.academy.learning_journal_team3.repository.EntryRepository;
import com.academy.learning_journal_team3.repository.UserRepository;
import com.academy.learning_journal_team3.service.EntryService;
import com.academy.learning_journal_team3.service.TopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EntryController {

    @Autowired
    private EntryService entryService;
    @Autowired
    private TopicsService topicsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("/entries")
    public String getEntries(Model model, Authentication authentication,
                             @RequestParam(value="personType", required = false) String personType,
                             @RequestParam(name="topic", required = false) Topic topicType) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);
        model.addAttribute("topics",topicsService.getAllTopics());
        List<Entry> entries;
        if("me".equals(personType) && topicType != null) {
            entries = entryService.getEntryByUser(user).stream()
                    .filter(e -> topicType.equals(e.getTopic())).collect(Collectors.toList());

        }else if("me".equals(personType)) {
           entries = entryService.getEntryByUser(user);

        }else if(topicType != null) {
            entries = entryRepository.findByTopic(topicType);
        }else{
            entries = entryService.getAllEntries();
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("entries", entries);
        return "entries";
    }

    @PostMapping("/entry/delete")
    public String deleteEntry(@RequestParam Long entryId) {
        entryService.deleteEntry(entryId);
        return "redirect:/entries";
    }

    @GetMapping("/newEntry/{id}")
    public String showEntryForUpdate(Model model, @PathVariable Long id) {
        Entry entry = entryService.getEntryById(id).orElseThrow();
        model.addAttribute("entry", entry);
        model.addAttribute("topics", topicsService.getAllTopics());
        return "newEntry";

    }

    @GetMapping("/newEntry")
    public String getNewEntry(Model model) {
        model.addAttribute("topics",topicsService.getAllTopics());
        model.addAttribute("entry", new Entry());
        return "newEntry";
    }

}




