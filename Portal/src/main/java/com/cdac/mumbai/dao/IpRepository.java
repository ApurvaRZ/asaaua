package com.cdac.mumbai.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.Ip;
import com.cdac.mumbai.model.IpKey;

@Repository
public interface IpRepository extends JpaRepository<Ip, IpKey> {
	//@Query(value = "select m from Ip m inner join User u on m.reg_id = u.reg_id where u.reg_id=?1")
//	@Query("SELECT u from Ip u where u.regId=?1")
//	Optional<List<Ip>> findAllRegId(Integer Reg_id);
	//@Query("SELECT DISTINCT u FROM Ip u WHERE u.regId = ?1")
//	@Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Ip i WHERE i.ip = :ip")
	boolean existsByRegIdAndIp(Integer regId, String ip);

	Optional<Ip> findByIp(String ip);
	List<Ip> findByRegId(Integer regId); 
}
