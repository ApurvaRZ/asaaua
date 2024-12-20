package com.cdac.mumbai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.cdac.mumbai.dto.CostSummary;
import com.cdac.mumbai.jwt.UserPrincipal;
import com.cdac.mumbai.service.CostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = "http://10.210.9.142:3000")
@RestController
@RequestMapping("/cost")
//@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class CostController {

    @Autowired
    private CostService costService;

    @GetMapping("/reg_id")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public List<CostSummary> getCostByRegId(@AuthenticationPrincipal UserPrincipal user) {
        log.info("Received request to get cost details for regId: {}", user.getRegId());

        List<CostSummary> costSummaryList;
        try {
            costSummaryList = costService.findByRegId(user.getRegId());
            log.debug("Cost details successfully retrieved for regId: {}", user.getRegId());
        } catch (Exception e) {
            log.error("Error occurred while retrieving cost details for regId: {}: {}", user.getRegId(), e.getMessage(), e);
            throw e;  // You may also choose to return a custom error response here.
        }

        return costSummaryList;
    }
}
