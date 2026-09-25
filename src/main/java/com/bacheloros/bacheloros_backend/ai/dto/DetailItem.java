package com.bacheloros.bacheloros_backend.ai.dto;

public class DetailItem {
    private String label;
    private String value;
    private String status; // "ok" | "over" | "info"

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
