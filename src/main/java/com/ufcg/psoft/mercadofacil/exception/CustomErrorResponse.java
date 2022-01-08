package com.ufcg.psoft.mercadofacil.exception;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CustomErrorResponse {

	private String message;

	private int status;

	private String path;

	private LocalDateTime timestamp;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<?> details;


	public CustomErrorResponse(String message, int status, String path) {
		this.message = message;
		this.status = status;
		this.path = path;
		this.timestamp = LocalDateTime.now(ZoneId.of("+00:00"));
	}

	public CustomErrorResponse(String message, int status, String path, List<?> details) {
		this(message, status, path);
		this.details = details;
	}


	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}

	public String getPath() {
		return path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public List<?> getDetails() {
		return details;
	}

	public void setDetails(List<?> details) {
		this.details = details;
	}

}
