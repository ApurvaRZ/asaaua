package com.cdac.mumbai.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.SaConfigPara;


@Repository
public interface SaConfigParaRepository extends JpaRepository<SaConfigPara, String> {
	@Query("SELECT paraValue from SaConfigPara pr WHERE paraName = :prName")
	 String findByParaName(String prName);
	
}
