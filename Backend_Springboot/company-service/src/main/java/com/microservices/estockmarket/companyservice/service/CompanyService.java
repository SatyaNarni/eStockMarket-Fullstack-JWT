package com.microservices.estockmarket.companyservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.estockmarket.companyservice.entity.Company;
import com.microservices.estockmarket.companyservice.repository.CompanyRepository;

@Service
public class CompanyService {
	@Autowired
	private CompanyRepository repository;

	public Company fetchCompany(String companyCode) {
		return repository.findById(companyCode).orElse(null);
	}

	public List<Company> fetchCompanies() {
		return repository.findAll();
	}

	public void deleteCompany(String companyCode) {
		repository.deleteById(companyCode);
	}

	public Company registerCompany(Company company) {
		return repository.save(company);
	}
}
