package com.microservices.estockmarket.companyservice.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMPANY_TBL")
public class Company {
	@Id
	private String companyCode;
	private String companyName;
	private String companyCEO;
	private BigDecimal companyTurnover;
	private String url;
	private String stockExchange;

	public Company() {
		super();
	}

	public Company(String companyCode, String companyName, String companyCEO, BigDecimal companyTurnover, String url,
			String stockExchange) {
		super();
		this.companyCode = companyCode;
		this.companyName = companyName;
		this.companyCEO = companyCEO;
		this.companyTurnover = companyTurnover;
		this.url = url;
		this.stockExchange = stockExchange;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyCEO() {
		return companyCEO;
	}

	public void setCompanyCEO(String companyCEO) {
		this.companyCEO = companyCEO;
	}

	public BigDecimal getCompanyTurnover() {
		return companyTurnover;
	}

	public void setCompanyTurnover(BigDecimal companyTurnover) {
		this.companyTurnover = companyTurnover;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStockExchange() {
		return stockExchange;
	}

	public void setStockExchange(String stockExchange) {
		this.stockExchange = stockExchange;
	}

	@Override
	public String toString() {
		return "Company [companyCode=" + companyCode + ", companyName=" + companyName + ", companyCEO=" + companyCEO
				+ ", companyTurnover=" + companyTurnover + ", url=" + url + ", stockExchange=" + stockExchange + "]";
	}

}
