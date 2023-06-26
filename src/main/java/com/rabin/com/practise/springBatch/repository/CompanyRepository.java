package com.rabin.com.practise.springBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rabin.com.practise.springBatch.entity.Company;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>{

}
