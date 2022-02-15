package com.microservices.estockmarket.companyservice.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.microservices.estockmarket.companyservice.entity.Company;
import com.microservices.estockmarket.companyservice.exception.SourceNotFoundException;
import com.microservices.estockmarket.companyservice.service.CompanyService;

@RestController
@CrossOrigin(origins= "*", allowedHeaders="*")
public class CompanyResource {

	@Autowired
	private CompanyService service;

	@Autowired
	private StockProxy proxy;

	// For Testing Purpose...
	@GetMapping("/company")
	public String home() {
		return "Welcome...Manasaaaa! ";
	}

	// http://localhost:8765/company-service/company/getall
	@GetMapping("/company/getall")
	//@RolesAllowed({ "developer" })
	// @RolesAllowed({"company_read"})
	public List<Company> companies() {
		System.out.println("COMINGGGGG....");
		List<Company> compList = service.fetchCompanies();
		if (compList == null) {
			throw new SourceNotFoundException("No Companies Registered");
		}
		return compList;
	}

	// http://localhost:8765/company-service/company/get/C03
	@GetMapping(path = "/company/get/{code}", produces = { "application/json" })
	public Company company(@PathVariable String code) {
		Company com = service.fetchCompany(code);
		if (com == null) {
			throw new SourceNotFoundException("No Company with code - " + code + " found");
		}
		return com;
	}

	@PutMapping(path = "/company/update/{code}", consumes = { "application/json" })
	public Company updateCompany(@PathVariable String code, @RequestBody Company company) {
		Company com = service.fetchCompany(code);
		if (com != null) {
			com.setCompanyCEO(company.getCompanyCEO());
			com.setUrl(company.getUrl());
			com.setCompanyTurnover(company.getCompanyTurnover());
		} else {
			throw new SourceNotFoundException("No Company with code - " + code + " found");
		}
		return service.registerCompany(com);
	}

	/*
	 * // http://localhost:8765/company-service/company/delete/C03
	 * 
	 * @DeleteMapping("/company/delete/{code}") public boolean delete(@PathVariable
	 * String code) { boolean res =service.deleteCompany(code); if ( res == false) {
	 * throw new SourceNotFoundException("No Company with code - " + code +
	 * " found to delete"); } proxy.deleteStocks(code); return res; }
	 */

	@DeleteMapping("/company/delete/{code}")
	public void delete(@PathVariable String code) {
		service.deleteCompany(code);
		proxy.deleteStocks(code);
	}

	// http://localhost:8765/company-service/company/register
	@PostMapping(path = "/company/register", consumes = { "application/json" })
	public ResponseEntity<Object> registerCompany(@RequestBody Company company) throws Exception {
		Company com = service.registerCompany(company);
		if (com == null) {
			throw new Exception("Company Registration Failed");
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{companyCode}")
				.buildAndExpand(com.getCompanyCode()).toUri();
		return ResponseEntity.created(location).build();
	}

}
