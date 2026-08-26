package com.bacheloros.bacheloros_backend.dto;

import java.util.List;

public class BillListResponse {
    private List<BillResponse> bills;
    private long upcomingCount;
    private long paidCount;
    private long overdueCount;

    public List<BillResponse> getBills() {
        return bills;
    }

    public void setBills(List<BillResponse> bills) {
        this.bills = bills;
    }

    public long getUpcomingCount() {
        return upcomingCount;
    }

    public void setUpcomingCount(long upcomingCount) {
        this.upcomingCount = upcomingCount;
    }

    public long getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(long paidCount) {
        this.paidCount = paidCount;
    }

    public long getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(long overdueCount) {
        this.overdueCount = overdueCount;
    }
}
