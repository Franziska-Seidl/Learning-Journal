package com.academy.learning_journal_team3.controller;


import com.academy.learning_journal_team3.entity.Topic;
import com.academy.learning_journal_team3.service.TopicsService;
import com.academy.learning_journal_team3.dto.TopicsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
public class TopicsController {

    @Autowired
    private TopicsService topicsService;

    @PostMapping("/createTopic")
    public String createTopics(@ModelAttribute TopicsModel topicsModel, RedirectAttributes redirectAttributes){
        try {
            Long topicsId = topicsService.createTopic(topicsModel);
            redirectAttributes.addAttribute("id", topicsId);
            redirectAttributes.addFlashAttribute("id", topicsId);
            redirectAttributes.addFlashAttribute("successMessage", "Thema erfolgreich erstellt");
            return "redirect:/topics";

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return"redirect:topics";
        }
    }

    @GetMapping("/topics")
    public String getTopics(Model model){
        List<Topic> topics = topicsService.getAllTopics();
        model.addAttribute("topics", topics);
        model.addAttribute("topicsModel", TopicsModel.builder().build());
        return "topics";
    }
}

