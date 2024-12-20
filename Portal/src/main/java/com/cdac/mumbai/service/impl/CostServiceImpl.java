package com.cdac.mumbai.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.dao.CostRepository;
import com.cdac.mumbai.dto.CostSummary;
import com.cdac.mumbai.exception.ApiRequestException;
import com.cdac.mumbai.exception.GeneralException;
import com.cdac.mumbai.mapper.CommonMapper;
import com.cdac.mumbai.service.CostService;
import com.cdac.mumbai.model.BillDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CostServiceImpl implements CostService {

    @Autowired
    CostRepository costRepository;


    @Override
    public List<CostSummary> findByRegId(Integer regId) {
        log.info("findByRegId called with regId: {}", regId);  // Logging the input parameter

        // Fetching cost data from the repository
        List<CostSummary> costSummaries = new ArrayList<>();
        try {
            log.debug("Fetching cost data from repository for regId: {}", regId);

            // Fetching the cost data based on regId
            List<BillDetails> billDetailsList = costRepository.findAllByRegId(regId);

            if (billDetailsList.isEmpty()) {
                log.warn("No cost data found for regId: {}", regId);  // Logging the case when no data is found
                throw new ApiRequestException("No cost data found for the given registration ID.", HttpStatus.NOT_FOUND);
            }

            // Manually converting BillDetails entities to CostSummary DTOs
            for (BillDetails billDetails : billDetailsList) {
                CostSummary costSummary = new CostSummary();
                costSummary.setBill_no(billDetails.getBill_no());
              //  costSummary.setReg_id(billDetails.getRegId());
                costSummary.setBill_year(billDetails.getBill_year());
                costSummary.setBill_quarter(billDetails.getBill_quarter());
                costSummary.setBill_amount(billDetails.getBill_amount());
                costSummary.setBill_generation_date(billDetails.getBill_generation_date());
                costSummary.setSurcharge(billDetails.getSurcharge());
                costSummary.setBill(billDetails.getBill());
                costSummary.setInvoice_no(billDetails.getInvoice_no());
                costSummary.setInvoice_date(billDetails.getInvoice_date());
                costSummary.setInvoice_amount(billDetails.getInvoice_amount());
                costSummary.setUpdate_by(billDetails.getUpdate_by());
                costSummary.setUpdate_timestamp(billDetails.getUpdate_timestamp());

                costSummaries.add(costSummary);
            }

            log.info("Successfully retrieved {} cost records for regId: {}", costSummaries.size(), regId);  // Logging the successful data retrieval

        } catch (ApiRequestException ae) {
            log.error("Error fetching cost data for regId: {}: {}", regId, ae.getMessage());  // Specific handling of known exceptions
            throw ae;  // Rethrow known exception

        } catch (Exception e) {
            log.error("Error occurred while fetching cost data for regId: {}: {}", regId, e.getMessage(), e);  // Logging unknown exceptions with stack trace
            throw new GeneralException("An unexpected error occurred while retrieving cost data.");
        }

        return costSummaries;
    }


    }

