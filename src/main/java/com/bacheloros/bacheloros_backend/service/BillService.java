package com.bacheloros.bacheloros_backend.service;

import com.bacheloros.bacheloros_backend.dto.BillListResponse;
import com.bacheloros.bacheloros_backend.dto.BillResponse;
import com.bacheloros.bacheloros_backend.dto.CreateBillRequest;
import com.bacheloros.bacheloros_backend.entity.Bill;
import com.bacheloros.bacheloros_backend.entity.BillStatus;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.exception.ResourceNotFoundException;
import com.bacheloros.bacheloros_backend.repository.BillRepository;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    public BillService(BillRepository billRepository,UserRepository userRepository) {
        this.billRepository = billRepository;
        this.userRepository= userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private BillResponse toResponse(Bill bill)
    {
        BillResponse billResponse=new BillResponse();
        billResponse.setId(bill.getId());
        billResponse.setAmount(bill.getAmount());
        billResponse.setDescription(bill.getDescription());
        billResponse.setDueDate(bill.getDueDate());
        billResponse.setPaid(bill.isPaid());
        billResponse.setPaidTo(bill.getPaidTo());
        billResponse.setRecurrenceType(bill.getRecurrenceType());
        billResponse.setRecurring(bill.isRecurring());
        billResponse.setTitle(bill.getTitle());
        billResponse.setPaidOn(bill.getPaidOn());
        return billResponse;
    }

    public BillResponse createBill(CreateBillRequest billRequest)
    {
        User user=getCurrentUser();
        Bill bill=new Bill();
        bill.setAmount(billRequest.getAmount());
        bill.setDescription(billRequest.getDescription());
        bill.setDueDate(billRequest.getDueDate());
        bill.setPaid(false);
        bill.setPaidTo(billRequest.getPaidTo());
        bill.setRecurrenceType(billRequest.getRecurrenceType());
        bill.setRecurring(billRequest.isRecurring());
        bill.setTitle(billRequest.getTitle());
        bill.setUser(user);
        return toResponse(billRepository.save(bill));
    }

    public List<BillResponse> getMyBills()
    {
        User user=getCurrentUser();
        return billRepository.findBillsByUser(user)
                .stream().map(bill -> toResponse(bill))
                .collect(Collectors.toList());
    }

    public BillResponse markAsPaid(Long billId) {
        User user = getCurrentUser();

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (!bill.getUser().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("You are not authorized to modify this bill");
        }

        bill.setPaid(true);
        bill.setPaidOn(LocalDate.now());
        billRepository.save(bill);

        return toResponse(bill);
    }
    public BillListResponse getBills(BillStatus status) {
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();

        List<Bill> filteredBills;
        switch (status) {
            case PAID:
                filteredBills = billRepository.findByUserAndIsPaidOrderByDueDateDesc(user, true);
                break;
            case OVERDUE:
                filteredBills = billRepository.findByUserAndIsPaidAndDueDateBeforeOrderByDueDateAsc(user, false, today);
                break;
            case UPCOMING:
            default:
                filteredBills = billRepository.findByUserAndIsPaidAndDueDateGreaterThanEqualOrderByDueDateAsc(user, false, today);
                break;
        }

        long upcomingCount = billRepository.countByUserAndIsPaidAndDueDateGreaterThanEqual(user, false, today);
        long paidCount = billRepository.countByUserAndIsPaid(user, true);
        long overdueCount = billRepository.countByUserAndIsPaidAndDueDateBefore(user, false, today);

        LocalDate weekEnd = today.plusDays(6);
        List<Bill> dueThisWeek = billRepository.findByUserAndDueDateBetweenAndIsPaid(user, today, weekEnd, false);
        BigDecimal dueThisWeekTotal = dueThisWeek.stream()
                .map(Bill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BillListResponse response = new BillListResponse();
        response.setBills(filteredBills.stream().map(this::toResponse).collect(Collectors.toList()));
        response.setUpcomingCount(upcomingCount);
        response.setPaidCount(paidCount);
        response.setOverdueCount(overdueCount);
        return response;
    }
    public BillResponse getBillById(Long id)
    {
        User currentUser = getCurrentUser();
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        if (!bill.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Bill not found"); // ownership check
        }
        return toResponse(bill);
    }
    public void deleteBillById(Long id)
    {
        User currentUser = getCurrentUser();
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        if (!bill.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Bill not found"); // ownership check
        }
        billRepository.delete(bill);
    }


}
