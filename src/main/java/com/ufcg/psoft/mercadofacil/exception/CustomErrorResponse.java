package com.ufcg.psoft.mercadofacil.exception;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomErrorResponse {

	@NonNull
	private String message;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<?> details;

	@NonNull
	private Integer status;

	@NonNull
	private String path;
	
	private LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("+00:00"));

	public CustomErrorResponse(String message, Integer status, String path, List<?> details) {
		this(message, status, path);
		this.details = details;
	}

}
