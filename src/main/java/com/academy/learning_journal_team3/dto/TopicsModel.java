package com.academy.learning_journal_team3.dto;

import lombok.Builder;

@Builder
public record TopicsModel(Long topicsId, String name) { }
