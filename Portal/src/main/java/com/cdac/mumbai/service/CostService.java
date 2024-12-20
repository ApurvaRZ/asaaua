package com.cdac.mumbai.service;

import java.util.List;

import com.cdac.mumbai.dto.CostSummary;

public interface CostService {

  List<CostSummary> findByRegId(Integer regId) ;

}
