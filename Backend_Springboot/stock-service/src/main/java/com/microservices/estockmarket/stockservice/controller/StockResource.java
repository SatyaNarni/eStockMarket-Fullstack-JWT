package com.microservices.estockmarket.stockservice.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.estockmarket.stockservice.exception.SourceNotFoundException;
import com.microservices.estockmarket.stockservice.model.Stock;
import com.microservices.estockmarket.stockservice.service.SequenceGeneratorService;
import com.microservices.estockmarket.stockservice.service.StockService;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class StockResource {
	@Autowired
	StockService service;
	
	@Autowired
	private SequenceGeneratorService seqService;

	// For Testing Purpose...
	@GetMapping("/stock")
	public String getStock() {
		return "Response from Stock Service, User Id:";
	}

	// http://localhost:8765/stock-service/stock/add
	@PostMapping(path = "/stock/add", consumes = { "application/json" })
	public void addStock(@RequestBody Stock stock) throws Exception {
		System.out.println("In Stockkk... "+stock);
		stock.setStockId(seqService.getSequenceNumber(Stock.SEQUENCE_NAME));
		System.out.println("In Stockkk1... "+stock);
		Stock s = service.addStock(stock);
		System.out.println("In Stockkk2... "+stock);
		if (s == null) {
			throw new Exception("Stcok Addition Unsuccessfull..Addition.");
		}
	}

	// http://localhost:8765/stock-service/stock/hateoas/add
	@PostMapping(path = "/stock/hateoas/add", consumes = { "application/json" })
	public EntityModel<Stock> addStockHateoas(@RequestBody Stock stock) throws Exception {
		Stock s = service.addStock(stock);
		if (s == null) {
			throw new Exception("Stcok Addition Unsuccessfull..Addition.");
		}
		EntityModel<Stock> model = EntityModel.of(stock);
		WebMvcLinkBuilder linkToStocks = linkTo(methodOn(this.getClass()).getAllStock());
		model.add(linkToStocks.withRel("all-stocks"));
		return model;
	}

	// http://localhost:8765/stock-service/stock/get/C01/2016-10-31T01:30:00.000-05:00/2020-10-31T01:30:00.000-05:00
	@GetMapping(path = "/stock/getPrices/{companyCode}/{start}/{end}", produces = { "application/json" })
	public List<BigDecimal> fetchStockPriceList2(@PathVariable String companyCode,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
		return service.fetchStockPriceList(companyCode, start, end);
	}

	// http://localhost:8765/stock-service/stock/get/C01?start=2016-10-31T01:30:00.000-05:00&end=2020-10-31T01:30:00.000-05:00
	@GetMapping("/stock/getPrices/usingparams/{companyCode}")
	public List<BigDecimal> fetchStockPriceList(@PathVariable String companyCode,
			@RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
			@RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
		return service.fetchStockPriceList(companyCode, start, end);
	}

	// http://localhost:8765/stock-service/stock/get/C01/stats/2016-10-31T01:30:00.000-05:00/2020-10-31T01:30:00.000-05:00
	@GetMapping("/stock/getusingparams/{companyCode}/stats/{start}/{end}")
	public DoubleSummaryStatistics getPriceStatsinBetween(@PathVariable String companyCode,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
		return service.getPriceStats(companyCode, start, end);
	}

	// http://localhost:8765/stock/get/C01/stats/2016-10-31T01:30:00.000-05:00/2020-10-31T01:30:00.000-05:00
		@GetMapping("/stock/stats/{companyCode}")
		public DoubleSummaryStatistics getPriceStats(@PathVariable String companyCode) {
			return service.getStats(companyCode);
		}
		
	// http://localhost:8765/stock-service/stock/getAll
	@GetMapping("/stock/getAll")
	public List<Stock> getAllStock() {
		List<Stock> stockList = service.getAllStock();
		if (stockList == null) {
			throw new SourceNotFoundException("No Stocks data found");
		}
		return stockList;
	}

	@GetMapping("/stock/getAll/{code}")
	public List<Stock> getAllStocksOfCompany(@PathVariable String code) {
		List<Stock> stockList = service.getAllStocksOfCompany(code);
		if (stockList == null) {
			throw new SourceNotFoundException("No Stocks data found");
		}
		return stockList;
	}

	// http://localhost:8765/stock-service/stocks/delete/C03
	@DeleteMapping("/stocks/delete/{code}")
	public void delete(@PathVariable String code) {
		service.deleteStocks(code);
	}

}
