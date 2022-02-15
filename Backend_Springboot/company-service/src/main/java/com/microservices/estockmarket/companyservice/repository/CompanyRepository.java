package com.microservices.estockmarket.companyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.estockmarket.companyservice.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
