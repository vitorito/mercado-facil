package com.ufcg.psoft.mercadofacil.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomErrorType extends RuntimeException {

	private HttpStatus status;
	
	private List<?> details;
	
    public CustomErrorType(String message, HttpStatus status) {
        super(message);
		this.status = status;
    }

	public CustomErrorType(String message, HttpStatus status, List<?> details) {
		this(message, status);
		this.details = details;
	}
	
}
