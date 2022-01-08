package com.ufcg.psoft.mercadofacil.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomErrorType.class)
	public ResponseEntity<CustomErrorResponse> handleCustomExceptions(
			CustomErrorType e,
			HttpServletRequest request) {
		CustomErrorResponse erro = new CustomErrorResponse(
				e.getMessage(),
				e.getStatus().value(),
				request.getRequestURI(),
				e.getDetails());
		
		return new ResponseEntity<>(erro, e.getStatus());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(
			Exception e,
			HttpServletRequest request) {
		CustomErrorResponse erro = new CustomErrorResponse(
				e.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				request.getRequestURI());

		return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> handleAnyException(
			Exception e,
			HttpServletRequest request) {
		CustomErrorResponse erro = new CustomErrorResponse(e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				request.getRequestURI());
		System.out.println(e.getStackTrace());

		return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
