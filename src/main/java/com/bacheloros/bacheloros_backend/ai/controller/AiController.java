package com.bacheloros.bacheloros_backend.ai.controller;

import com.bacheloros.bacheloros_backend.ai.dto.AiResponse;
import com.bacheloros.bacheloros_backend.ai.dto.AskAiRequest;
import com.bacheloros.bacheloros_backend.ai.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public AiResponse ask(@RequestBody AskAiRequest request) {
        return aiService.askAi(request.getQuestion());
    }
}
