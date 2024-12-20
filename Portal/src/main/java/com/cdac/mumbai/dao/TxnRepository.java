package com.cdac.mumbai.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.TransStatsUser;
import com.cdac.mumbai.dto.TxnDetails;
import com.cdac.mumbai.model.TransStatsId;

@Repository
public interface TxnRepository extends JpaRepository<TransStatsUser,TransStatsId> {
	//@Query(value = "select m from TransStats m inner join User u on m.reg_id = u.reg_id where u.reg_id=?1")
	//List<TransStats> findAllByReg_id(Integer reg_id);
	Optional<List<TransStatsUser>> findAllByRegId(Integer regId);
	//List<TransStats> findAllByYear_Month(String year_month);
	@Query("SELECT SUM(trans_count) from TransStatsUser stats where transaction_type = 'AUTH'")
	String getAuthCount(String auth);
	@Query("SELECT SUM(trans_count) from TransStatsUser stats where transaction_type = 'OTP'")
	String getOtpCount(String otp);
	@Query("SELECT SUM(trans_count) from TransStatsUser stats where transaction_type = 'e-KYC'")
	String getKycCount(String kyc);
	@Query("SELECT SUM(trans_count) from TransStatsUser stats")
	String getAllCount();
	@Query("SELECT t.year_month,SUM(trans_count) as count FROM TransStatsUser t GROUP BY t.year_month ORDER BY t.year_month DESC")
	List<Object[]> get4MonthWiseCount(Pageable pageable);
}
