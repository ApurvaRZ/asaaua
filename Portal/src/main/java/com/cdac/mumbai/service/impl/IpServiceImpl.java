package com.cdac.mumbai.service.impl;

import static com.cdac.mumbai.constants.Message.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.common.SendMail;
import com.cdac.mumbai.dao.IpRepository;
import com.cdac.mumbai.dao.UserRepository;
import com.cdac.mumbai.dto.ClientIp;
import com.cdac.mumbai.exception.ApiRequestException;
import com.cdac.mumbai.exception.GeneralException;
import com.cdac.mumbai.mapper.CommonMapper;
import com.cdac.mumbai.model.Ip;
import com.cdac.mumbai.model.User;
import com.cdac.mumbai.service.IpService;
import com.cdac.mumbai.service.SaConfigParaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class IpServiceImpl implements IpService {

    @Autowired
    IpRepository IP;

    @Autowired
    ModelMapper mm;

    @Autowired
    CommonMapper cm;

    @Autowired
    UserRepository ur;

    @Autowired
    SaConfigParaService sa;

    @Autowired
    SendMail sm;

    @Override
    public String saveIp(String ip, String username) {
        log.info("Starting saveIp process for IP: {} and user: {}", ip, username);

       

        // Find user by username
        log.info("Fetching user details for username: {}", username);
        User usr = ur.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Username {} not found in the database", username);
                    return new ApiRequestException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND);
                });
        log.info("User {} found with regId: {}", username, usr.getRegId());
        
        // Check if the IP is already in use
        if (IP.existsByRegIdAndIp(usr.getRegId(),ip)) {
            log.error("IP {} is already in use", ip);
            throw new GeneralException(IP_IN_USE);
        }
        log.info("IP {} is not currently in use", ip);

        // Validate the maximum allowed IPs
        log.info("Fetching maximum allowed IPs from configuration");
        int maxIpAllowed = Integer.parseInt(sa.getSaConfigKeys("maxnumberofip").toString().trim());
        int currentIpCount = this.findByRegid(usr.getRegId()).size();
        log.debug("Max IPs allowed: {}, Current IP count for user {}: {}", maxIpAllowed, username, currentIpCount);

        if (maxIpAllowed < currentIpCount) {
            log.error("User {} has exceeded the allowed number of IPs. Current count: {}, Allowed: {}", 
                      username, currentIpCount, maxIpAllowed);
            throw new GeneralException(IP_ALLOWED);
        }
        log.info("User {} is within the allowed IP limit. Proceeding to whitelist IP.", username);

        // Send whitelisting email
        log.info("Sending whitelisting email to admin for user: {}", username);
        String mailResult = sm.whitelistingMailToAdmin(usr, ip);
        if ("failtosendmail".equals(mailResult)) {
            log.error("Failed to send whitelisting email to admin for user: {}", username);
            throw new GeneralException(EMAIL_FAIL_ADMIN);
        }
        log.info("Whitelisting email sent successfully for user: {}", username);

        // Save IP
        log.info("Preparing to save IP: {} for user: {}", ip, username);
        Ip saveIp = new Ip();
        saveIp.setIp(ip);
        saveIp.setReg_id(usr.getRegId());
        saveIp.setWhitlist_timestamp(new Date(System.currentTimeMillis()));
        saveIp.setStatus(NEW_REQUEST);
           IP.save(saveIp);
        // Uncomment this when integrating with database save logic
        // Ip savedIp = IP.save(mm.map(ip, Ip.class));
        log.info("IP {} has been successfully saved for user: {}", ip, username);

        return "Successfully saved IP";
    }

    @Override
    public String deleteIpById(String ip) {
        log.info("Attempting to delete IP: {}", ip);

        Ip ipToDelete = IP.findByIp(ip)
                .orElseThrow(() -> {
                    log.error("IP {} not found", ip);
                    return new ApiRequestException(IP_NOT_FOUND, HttpStatus.NOT_FOUND);
                });

        User usr = ur.findByregId(ipToDelete.getReg_id());

        String mailResult = sm.deleteWhitelistedIpMailToAdmin(usr, ip);
        if ("failtosendmail".equals(mailResult)) {
            log.error("Failed to send deletion email to admin for IP: {}", ip);
            throw new GeneralException(EMAIL_FAIL_ADMIN);
        }

        IP.delete(ipToDelete);

        log.info("Successfully processed deletion request for IP: {}", ip);
        return "Whitelisted IP deletion request processed successfully";
    }

    @Override
    public List<ClientIp> findByRegid(int regId) {
        log.info("Fetching IPs for regId: {}", regId);
        
       List<Ip> ips = IP.findByRegId(regId); // Fetch IPs using the repository
        
        if (ips.isEmpty()) {
            log.warn("No IPs found for regId: {}", regId);
        } else {
            log.info("Found {} IP(s) for regId: {}", ips.size(), regId);
        }
        
        log.info("Fetched IP: {}", ips);
        ips.forEach(ip -> log.info("IP: {}, Status: {}, WhitlistTimestamp: {}", ip.getIp(), ip.getStatus(), ip.getWhitlist_timestamp()));

        return ips.stream()
                .map(ip -> new ClientIp(ip.getIp(), ip.getStatus(), ip.getWhitlist_timestamp()))
                .collect(Collectors.toList());
    }
}
