package com.bacheloros.bacheloros_backend.ai.service;

import com.bacheloros.bacheloros_backend.ai.dto.AiFinanceContext;
import com.bacheloros.bacheloros_backend.ai.dto.AiResponse;
import com.bacheloros.bacheloros_backend.budget.service.BudgetService;
import com.bacheloros.bacheloros_backend.config.RestTemplateConfig;
import com.bacheloros.bacheloros_backend.service.BillService;
import com.bacheloros.bacheloros_backend.service.ExpenseService;
import com.bacheloros.bacheloros_backend.service.FinanceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {
    private final RestTemplate restTemplate;
    private final FinanceService financeService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final BillService billService;

    public AiService(RestTemplate restTemplate, FinanceService financeService,
                     BudgetService budgetService, ExpenseService expenseService,
                     BillService billService) {
        this.restTemplate = restTemplate;
        this.financeService = financeService;
        this.budgetService = budgetService;
        this.expenseService = expenseService;
        this.billService = billService;
    }

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    private AiFinanceContext buildFinanceContext() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();
        LocalDate prevMonthDate = today.minusMonths(1);

        AiFinanceContext context = new AiFinanceContext();
        context.setCurrentMonthSummary(financeService.getOverview());
        context.setCurrentMonthBudget(budgetService.getOverview(month, year));
        context.setPreviousMonthBudget(budgetService.getOverview(
                prevMonthDate.getMonthValue(), prevMonthDate.getYear()));
        context.setCurrentMonthExpenses(expenseService.getExpensesByMonth(month, year));
        context.setAllBills(billService.getMyBills());

        return context;
    }

    public AiResponse askAi(String question) {

        // Step 1: existing full context finance data fetch karo (already bana hua service reuse)
        AiFinanceContext context = buildFinanceContext();


        // Step 2: Python ko bhejne ke liye payload banao
        Map<String, Object> payload = new HashMap<>();
        payload.put("question", question);
        payload.put("financeData", context);

        // Step 3: Python service ko call karo
//        Map response = restTemplate.postForObject(
//                aiServiceUrl + "/ask",
//                payload,
//                Map.class
//        );
//
//        // Step 4: jawab nikaal kar wapas bhejo
//        String answer = (String) response.getOrDefault("answer","no data available");
//        return new AiResponse(answer);
        AiResponse response = restTemplate.postForObject(
                aiServiceUrl + "/ask",
                payload,
                AiResponse.class
        );

        return response;
    }

}
