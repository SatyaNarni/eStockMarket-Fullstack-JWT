package com.microservices.estockmarket.companyservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SourceNotFoundException extends RuntimeException {
	public SourceNotFoundException(String message) {
		super(message);
	}
}
