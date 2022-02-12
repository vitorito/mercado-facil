package com.ufcg.psoft.mercadofacil.exception;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomErrorResponse {

	private String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private CustomErrorResponseDetails details;

	private Integer status;

	private String path;

	private final LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("+00:00"));

}
