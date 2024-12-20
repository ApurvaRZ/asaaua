package com.cdac.mumbai.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.Pr_Agreement;


@Repository
public interface Pr_AgreementRepository  extends JpaRepository<Pr_Agreement, Integer>{

}
