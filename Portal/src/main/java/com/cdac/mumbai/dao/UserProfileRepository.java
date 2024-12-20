package com.cdac.mumbai.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.UserProfile;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer>{
	 List<UserProfile> findByType(String name);
}
