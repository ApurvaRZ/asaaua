package com.cdac.mumbai.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.BillDetails;
import com.cdac.mumbai.model.Ip;


@Repository
public interface CostRepository extends JpaRepository<BillDetails, Integer> {
	
    
	List<BillDetails> findAllByRegId(Integer regId);
}