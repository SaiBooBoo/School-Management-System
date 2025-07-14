package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dto.FeeDto;
import org.example.athenabackend.dtoSummaries.FeeSummaryDto;
import org.example.athenabackend.request.MassFeeRequest;
import org.example.athenabackend.model.FeeStatus;
import org.example.athenabackend.service.FeeService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
public class FeeController {
    private final FeeService feeService;

    @GetMapping("/parent/{id}")
    public Page<FeeDto> getFeesForParent(@PathVariable Integer id,
                                         @RequestParam(required = false) FeeStatus status,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size){
        return feeService.getFeesForParent(id, status, page, size);
    }

    @GetMapping("/unpaid")
    public FeeSummaryDto unpaidFees(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year){
        if (month != null && year != null){
            return feeService.getUnpaidFeesSummary(month, year);
        }
        return feeService.getUnpaidFeesSummaryCurrentMonth();
    }

    @GetMapping
    public List<FeeDto> getAllFees(){
        return feeService.getAllFees();
    }

    @GetMapping("/student/{studentId}")
    public List<FeeDto> getFeesByStudentId(@PathVariable Integer studentId){
        return feeService.getByStudent(studentId);
    }

    @GetMapping("/status/{status}")
    public List<FeeDto> getFeesByStatus(@PathVariable FeeStatus status){
        return feeService.getByStatus(status);
    }

    @GetMapping("/due")
    public List<FeeDto> getFeesByDueDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return feeService.getByDueDateBetween(start, end);
    }

    @PostMapping
    public ResponseEntity<FeeDto> addFee(@RequestBody FeeDto dto){
        FeeDto saved = feeService.addFee(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> addFeesToAllStudents(@RequestBody MassFeeRequest dto){
        feeService.createFeeForAllStudents(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Fees created successfully");
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<FeeDto> markFeeAsPaid(
            @PathVariable Integer id,
            @RequestBody FeeDto dto){
        return ResponseEntity.ok(feeService.updateFeeStatus(id, dto.getPaymentDate()));
    }

}
