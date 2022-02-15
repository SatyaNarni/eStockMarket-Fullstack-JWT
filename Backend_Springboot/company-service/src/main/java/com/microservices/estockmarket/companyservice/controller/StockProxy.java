package com.microservices.estockmarket.companyservice.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-gateway")
public interface StockProxy {
	@DeleteMapping("stock-service/stocks/delete/{code}")
	public void deleteStocks(@PathVariable String code);

}
