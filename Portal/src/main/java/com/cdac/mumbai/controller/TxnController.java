package com.cdac.mumbai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.mumbai.dto.TxnSummary;
import com.cdac.mumbai.jwt.UserPrincipal;
import com.cdac.mumbai.service.TxnService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/txns")
@CrossOrigin(origins = "*")
@Slf4j
//@SecurityRequirement(name = "bearerAuth")
public class TxnController {
    
    @Autowired
    private TxnService txnService;

    @GetMapping("/reg_id")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public List<TxnSummary> getTransactionsByRegId(@AuthenticationPrincipal UserPrincipal user) {
        log.info("Request received to get transactions for regId: {}", user.getRegId());
        
        try {
            List<TxnSummary> txnSummaryList = txnService.findByRegId(user.getRegId());
            log.debug("Transactions successfully retrieved for regId: {}",user.getRegId());
            return txnSummaryList;
        } catch (Exception e) {
            log.error("Error occurred while retrieving transactions for regId: {}: {}", user.getRegId(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/authCount")
    public ResponseEntity<String> authCount() {
        log.info("Request received to get authentication count.");
        
        try {
            String result = txnService.authCount();
            log.debug("Authentication count successfully retrieved: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error occurred while retrieving authentication count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/otpCount")
    public ResponseEntity<String> otpCount() {
        log.info("Request received to get OTP count.");
        
        try {
            String result = txnService.otpCount();
            log.debug("OTP count successfully retrieved: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error occurred while retrieving OTP count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/kycCount")
    public ResponseEntity<String> kycCount() {
        log.info("Request received to get KYC count.");
        
        try {
            String result = txnService.kycCount();
            log.debug("KYC count successfully retrieved: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error occurred while retrieving KYC count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/totalCount")
    public ResponseEntity<String> count() {
        log.info("Request received to get total count.");
        
        try {
            String result = txnService.count();
            log.debug("Total count successfully retrieved: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error occurred while retrieving total count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/monthWiseCount")
    public ResponseEntity<List<Object[]>> monthWiseCount() {
        log.info("Request received to get month-wise count.");
        
        try {
            List<Object[]> result = txnService.get4MonthCount();
            log.debug("Month-wise count successfully retrieved.");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error occurred while retrieving month-wise count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
