package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.BillListResponse;
import com.bacheloros.bacheloros_backend.dto.BillResponse;
import com.bacheloros.bacheloros_backend.dto.CreateBillRequest;
import com.bacheloros.bacheloros_backend.entity.BillStatus;
import com.bacheloros.bacheloros_backend.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public ResponseEntity<BillResponse> createBill(@RequestBody CreateBillRequest request) {
        BillResponse response = billService.createBill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<BillResponse> markAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(billService.markAsPaid(id));
    }
    @GetMapping
    public ResponseEntity<BillListResponse> getMyBills(
            @RequestParam(defaultValue = "UPCOMING") BillStatus status) {
        return ResponseEntity.ok(billService.getBills(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long id) {

        return  ResponseEntity.ok(billService.getBillById(id));// ya jo bhi mapping-pattern already use kar rahe ho
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillById(@PathVariable Long id) {
           billService.deleteBillById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}