package com.cdac.mumbai.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.model.VerificationToken;



@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
   VerificationToken findByToken(String token);
   @Modifying
   @Transactional
   @Query("DELETE FROM VerificationToken vt WHERE tokenId = ?1")
   void deleteByTokenId(Integer tokenId);
}
