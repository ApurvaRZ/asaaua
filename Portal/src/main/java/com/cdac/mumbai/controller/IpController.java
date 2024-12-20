package com.cdac.mumbai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.mumbai.dto.ClientIp;
import com.cdac.mumbai.jwt.UserPrincipal;
import com.cdac.mumbai.service.IpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ip")
@Slf4j
//@CrossOrigin(origins = "http://10.210.9.142:3000")
@SecurityRequirement(name = "bearerAuth")
public class IpController {

    @Autowired
    private IpService ipService;

    @PostMapping("/save")
    public ResponseEntity<String> saveIPAddress(@AuthenticationPrincipal UserPrincipal user, @RequestParam("ip") String clientIp) {
        log.info("Request to save IP address for user: {}", user.getUsername());

        try {
            String savedIp = ipService.saveIp(clientIp, user.getUsername());
            log.debug("IP address successfully saved for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedIp);
        } catch (Exception e) {
            log.error("Error occurred while saving IP address for user: {}: {}", user.getUsername(), e.getMessage(), e);
            throw e; // Rethrowing the exception to be handled by a global exception handler if any
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClientIp>> getIPAddressesByRegId(@AuthenticationPrincipal UserPrincipal user) {
        log.info("Request to list IP addresses for user with regId: {}", user.getRegId());

        try {
            List<ClientIp> ipAddresses = ipService.findByRegid(user.getRegId());
            log.debug("IP addresses successfully retrieved for regId: {}", user.getRegId());
            return ResponseEntity.ok(ipAddresses);
        } catch (Exception e) {
            log.error("Error occurred while retrieving IP addresses for regId: {}: {}", user.getRegId(), e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete IP by ID")
    public ResponseEntity<String> deleteIp(@RequestParam("ip")  String ip) {
        log.info("Request to delete IP address: {}", ip);

        try {
            String response = ipService.deleteIpById(ip);
            log.debug("IP address successfully deleted: {}", ip);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while deleting IP address: {}: {}", ip, e.getMessage(), e);
            throw e;
        }
    }
}
