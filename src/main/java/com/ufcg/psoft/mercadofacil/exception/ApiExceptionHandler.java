package com.ufcg.psoft.mercadofacil.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomErrorType.class)
	public ResponseEntity<Object> handleCustomExceptions(
			CustomErrorType e,
			WebRequest request) {
		HttpStatus status = e.getStatus();
		var response = geraResponse(e.getMessage(), status, request, e.getDetails());
		
		return new ResponseEntity<>(response, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAnyException(
			Exception ex,
			WebRequest request) {
		return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage())
				.collect(Collectors.toList());

		var details = CustomErrorResponseDetails.builder()
				.titulo("Campos inválidos e seus respectivos erros.")
				.itens(fieldErrors)
				.build();

		String message = "Erro na validação dos campos.";
		var erro = new CustomErrorType(message, HttpStatus.BAD_REQUEST, details);

		return handleCustomExceptions(erro, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex,
			@Nullable Object body,
			@Nullable HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		String message = ex.getLocalizedMessage();
		if (ex.getCause() != null) {
			message = ex.getCause().getMessage();
		}
		var response = geraResponse(message, status, request, null);

		return new ResponseEntity<>(response, status);
	}

	private CustomErrorResponse geraResponse(
			String message,
			HttpStatus status,
			WebRequest request,
			@Nullable CustomErrorResponseDetails details) {
		return CustomErrorResponse.builder()
				.message(message)
				.details(details)
				.status(status.value())
				.path(((ServletWebRequest) request).getRequest().getRequestURL().toString())
				.build();
	}

}
