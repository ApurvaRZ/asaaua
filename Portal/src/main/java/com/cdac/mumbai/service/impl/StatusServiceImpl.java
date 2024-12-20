package com.cdac.mumbai.service.impl;

import static com.cdac.mumbai.constants.Message.USERNAME_NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.dao.StatusRepository;
import com.cdac.mumbai.dao.UserRepository;
import com.cdac.mumbai.exception.ApiRequestException;
import com.cdac.mumbai.model.User;
import com.cdac.mumbai.service.StatusService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class StatusServiceImpl implements StatusService {

    @Autowired
    StatusRepository sr;

    @Autowired
    UserRepository ur;

    @Override
    public String getStatus(String username) {
        log.info("Fetching status for username: {}", username);

        // Find user by username
        User usr = ur.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new ApiRequestException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND);
                });

        log.debug("User found: {}", usr);

        // Get user status
        String status = sr.findStatus(usr.getStatus());
        log.info("Status for username {}: {}", username, status);

        return status;
    }
    
}
