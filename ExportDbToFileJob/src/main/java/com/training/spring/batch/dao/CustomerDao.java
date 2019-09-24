package com.training.spring.batch.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.spring.batch.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {

}
