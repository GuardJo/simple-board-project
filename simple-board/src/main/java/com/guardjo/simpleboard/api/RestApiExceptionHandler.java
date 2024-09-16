package com.guardjo.simpleboard.api;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice(basePackages = "com.guardjo.simpleboard.api")
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({EntityNotFoundException.class})
	public ResponseEntity<String> handleBadRequest(Exception e) {
		log.error("Error, {}", e.getMessage(), e);

		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({SecurityException.class})
	public ResponseEntity<String> handleUnauthorized(Exception e) {
		log.error("Error, {}", e.getMessage(), e);

		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}
}
