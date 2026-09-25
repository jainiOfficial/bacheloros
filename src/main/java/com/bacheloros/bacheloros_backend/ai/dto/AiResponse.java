package com.bacheloros.bacheloros_backend.ai.dto;

import java.util.List;

public class AiResponse {
    private String summary;
    private List<DetailItem> details;

    public AiResponse() {}  // Jackson ke liye zaroori

    public AiResponse(String summary, List<DetailItem> details) {
        this.summary = summary;
        this.details = details;
    }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<DetailItem> getDetails() { return details; }
    public void setDetails(List<DetailItem> details) { this.details = details; }

}
