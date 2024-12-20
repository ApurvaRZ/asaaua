package com.cdac.mumbai.service.impl;

import static com.cdac.mumbai.constants.Message.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.dao.TxnRepository;
import com.cdac.mumbai.dto.TxnSummary;
import com.cdac.mumbai.exception.ApiRequestException;
import com.cdac.mumbai.model.TransStatsUser;
import com.cdac.mumbai.service.TxnService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TxnServiceImpl implements TxnService {

    @Autowired
    TxnRepository txn;

    @Autowired
    ModelMapper mm;

    @Override
    public List<TxnSummary> findByRegId(Integer regId) {
        log.info("Fetching transaction details for registration ID: {}", regId);
        
        // Fetch transaction details for the given regId
        List<TransStatsUser> transStatsList = txn.findAllByRegId(regId)
                .orElseThrow(() -> {
                    log.error("Transaction details not found for registration ID: {}", regId);
                    return new ApiRequestException(TXNDETAILS_NOT_FOUND, HttpStatus.NOT_FOUND);
                });

        log.info("Transaction details found for registration ID: {}", regId);

        // Map TransStatsUser to TxnSummary directly instead of using ModelMapper (more control)
        List<TxnSummary> txnSummaryList = transStatsList.stream()
                .map(transStats -> {
                    TxnSummary txnSummary = new TxnSummary();
                    txnSummary.setYearmonth(transStats.getYear_month());
                    txnSummary.setTxncount(BigInteger.valueOf(transStats.getTrans_count()));
                    txnSummary.setEnvironment(transStats.getEnvironment());
                    txnSummary.setTxnType(transStats.getTransaction_type());
                    return txnSummary;
                })
                .collect(Collectors.toList());

        log.info("Transaction summary successfully mapped for registration ID: {}", regId);
        return txnSummaryList;
    }

    @Override
    public String authCount() {
        log.info("Fetching authorization count.");
        String auth = "AUTH";
        String count = txn.getAuthCount(auth);
        log.info("Authorization count fetched successfully: {}", count);
        return count;
    }

    @Override
    public String otpCount() {
        log.info("Fetching OTP count.");
        String otp = "OTP";
        String count = txn.getOtpCount(otp);
        log.info("OTP count fetched successfully: {}", count);
        return count;
    }

    @Override
    public String kycCount() {
        log.info("Fetching KYC count.");
        String kyc = "e-KYC";
        String count = txn.getKycCount(kyc);
        log.info("KYC count fetched successfully: {}", count);
        return count;
    }

    @Override
    public String count() {
        log.info("Fetching total transaction count.");
        String totalCount = txn.getAllCount();
        log.info("Total transaction count fetched successfully: {}", totalCount);
        return totalCount;
    }

    @Override
    public List<Object[]> get4MonthCount() {
        log.info("Fetching transaction counts for the last 4 months.");
        Pageable pageable = PageRequest.of(0, 4);
        
        List<Object[]> count = txn.get4MonthWiseCount(pageable);
        log.info("Transaction counts for the last 4 months fetched successfully.");

        return count;
    }
}
