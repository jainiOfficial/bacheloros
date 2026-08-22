package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.BillResponse;
import com.bacheloros.bacheloros_backend.dto.CreateBillRequest;
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

    @GetMapping
    public ResponseEntity<List<BillResponse>> getMyBills() {
        return ResponseEntity.ok(billService.getMyBills());
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<BillResponse> markAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(billService.markAsPaid(id));
    }
}