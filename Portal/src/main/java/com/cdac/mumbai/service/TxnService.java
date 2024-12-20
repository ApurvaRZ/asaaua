package com.cdac.mumbai.service;

import java.util.List;

import com.cdac.mumbai.dto.TxnDetails;
import com.cdac.mumbai.dto.TxnSummary;

public interface TxnService {
	List<TxnSummary> findByRegId(Integer regId);
	String authCount();
	String otpCount();
	String kycCount();
	String count();
	List<Object[]> get4MonthCount();
}
