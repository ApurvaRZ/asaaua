package com.cdac.mumbai.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.CurrentUserStatus;


@Repository
public interface StatusRepository extends JpaRepository<CurrentUserStatus, String> {
	
	@Query("SELECT status from CurrentUserStatus pr WHERE statusCode = :code")
	 String findStatus(String code);

}
