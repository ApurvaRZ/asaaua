package com.cdac.mumbai.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.mumbai.model.Mstate;



@Repository
public interface StateRepository extends JpaRepository<Mstate, String> {

}
