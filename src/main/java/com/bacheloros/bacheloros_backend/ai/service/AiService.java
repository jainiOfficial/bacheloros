package com.bacheloros.bacheloros_backend.ai.service;

import com.bacheloros.bacheloros_backend.ai.dto.AiResponse;
import com.bacheloros.bacheloros_backend.config.RestTemplateConfig;
import com.bacheloros.bacheloros_backend.service.FinanceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {
    private final RestTemplate restTemplate;
    private final FinanceService financeService;

    public AiService(RestTemplate restTemplate,FinanceService financeService)
    {
        this.financeService=financeService;
        this.restTemplate=restTemplate;
    }

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    public AiResponse askAi(String question) {
        // Step 1: existing finance data fetch karo (already bana hua service reuse)
        var financeData = financeService.getOverview(); // ya jo bhi method signature hai

        // Step 2: Python ko bhejne ke liye payload banao
        Map<String, Object> payload = new HashMap<>();
        payload.put("question", question);
        payload.put("financeData", financeData);

        // Step 3: Python service ko call karo
        Map response = restTemplate.postForObject(
                aiServiceUrl + "/ask",
                payload,
                Map.class
        );

        // Step 4: jawab nikaal kar wapas bhejo
        String answer = (String) response.getOrDefault("answer","no data available");
        return new AiResponse(answer);
    }

}
