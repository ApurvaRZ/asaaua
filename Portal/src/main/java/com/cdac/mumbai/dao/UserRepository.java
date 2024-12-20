package com.cdac.mumbai.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	//boolean existsByCdemail(String email);
	User findByregId(Integer regid);
	User findByCdemail(String email);
	@Query("SELECT count(organizationName) FROM User u WHERE status IN ('DE', 'IC', 'SD')")
	Integer deptCount();
	Optional<User> findByUsername(String username);
	boolean existsByCdemail(String email);
	
}
