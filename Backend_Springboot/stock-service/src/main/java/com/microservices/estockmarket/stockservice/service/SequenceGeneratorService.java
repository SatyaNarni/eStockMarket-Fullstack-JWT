package com.microservices.estockmarket.stockservice.service;


import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.microservices.estockmarket.stockservice.model.DbSequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;


@Service
public class SequenceGeneratorService {
	@Autowired
	private MongoOperations mongoOperations;
	
	public int getSequenceNumber(String sequenceName) {
		// get the seq
		Query query =new Query(Criteria.where("id").is(sequenceName));
		// update the seq
		Update update = new Update().inc("seq", 1);
		// modify in document
		final DbSequence counter = mongoOperations.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true).upsert(true), DbSequence.class);
 
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
		
		
	}
}
