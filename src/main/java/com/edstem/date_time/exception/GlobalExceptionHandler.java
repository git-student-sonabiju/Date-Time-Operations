package com.edstem.date_time.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoOverlapException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Map<String, String>> handleNoOverlapException(NoOverlapException ex) {
		Map<String, String> response = new HashMap<>();
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
