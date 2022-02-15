package com.microservices.estockmarket.stockservice.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.estockmarket.stockservice.model.Stock;
import com.microservices.estockmarket.stockservice.repository.StockRepository;

@Service
public class StockService {
	@Autowired
	private StockRepository repository;

	public Stock addStock(Stock stock) {
		return repository.save(stock);
	}

	public List<BigDecimal> fetchStockPriceList(String companyCode, Date start, Date end) {
		List<Stock> stockList = repository.findStockeByDtRange(start, end, companyCode);
		return stockList.stream().map(Stock::getStockPrice).collect(Collectors.toList());
		// return repository.findBytimeStampBetween(start,end);
	}

	public DoubleSummaryStatistics getPriceStats(String companyCode, Date start, Date end) {
		List<Stock> stockList = repository.findStockeByDtRange(start, end, companyCode);
		return stockList.stream().collect(Collectors.summarizingDouble(s -> s.getStockPrice().doubleValue()));
	}
	
	public DoubleSummaryStatistics getStats(String companyCode) {
		List<Stock> stockList = repository.findAllByCompanyCode(companyCode);
		return stockList.stream().collect(Collectors.summarizingDouble(s -> s.getStockPrice().doubleValue()));
	}

	public List<Stock> getAllStock() {
		return repository.findAll();
	}

	public List<Stock> getAllStocksOfCompany(String code) {
		return repository.findAllByCompanyCode(code);
	}

	public void deleteStocks(String code) {
		repository.deleteByCompanyCode(code);
	}
}
