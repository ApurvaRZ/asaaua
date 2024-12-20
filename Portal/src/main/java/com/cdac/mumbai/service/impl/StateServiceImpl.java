package com.cdac.mumbai.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.dao.StateRepository;
import com.cdac.mumbai.model.Mstate;
import com.cdac.mumbai.service.StateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class StateServiceImpl implements StateService {

    @Autowired
    StateRepository sr;

    @Override
    public List<Mstate> getState() {
        log.info("Fetching all states");

        List<Mstate> states = sr.findAll();

        if (states.isEmpty()) {
            log.warn("No states found in the database.");
        } else {
            log.info("Successfully retrieved {} states.", states.size());
        }

        return states;
    }
}
