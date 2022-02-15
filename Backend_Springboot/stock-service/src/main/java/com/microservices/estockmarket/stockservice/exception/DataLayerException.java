package com.microservices.estockmarket.stockservice.exception;

@SuppressWarnings("serial")
public class DataLayerException extends Exception {
	public DataLayerException(String message) {
		super(message);
	}
}
