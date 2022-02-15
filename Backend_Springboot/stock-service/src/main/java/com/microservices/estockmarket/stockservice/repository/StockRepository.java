package com.microservices.estockmarket.stockservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.microservices.estockmarket.stockservice.model.Stock;

@EnableMongoRepositories
public interface StockRepository extends MongoRepository<Stock, String> {
	// @Query(value = "{'name' : ?0}", fields = "{'description' : 0}")
	@Query(value = "{timeStamp : {$gte : ?0, $lte : ?1},companyCode:?2}")
	List<Stock> findStockeByDtRange(Date start, Date end, String companyCode);

	void deleteByCompanyCode(String code);

	List<Stock> findAllByCompanyCode(String code);

	// List<Stock> findBytimeStampBetween(Date start, Date end);
}
