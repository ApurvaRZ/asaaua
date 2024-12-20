package com.cdac.mumbai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.dao.SaConfigParaRepository;
import com.cdac.mumbai.service.SaConfigParaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SaConfigParaServiceImpl implements SaConfigParaService {

    @Autowired
    SaConfigParaRepository sr;

    @Override
    public String getSaConfigKeys(String name) {
        log.info("Fetching SaConfigPara key for parameter name: {}", name);

        String configValue = sr.findByParaName(name);

        if (configValue != null) {
            log.info("Successfully retrieved config value for parameter name: {}", name);
        } else {
            log.warn("No config value found for parameter name: {}", name);
        }

        return configValue;
    }
}
