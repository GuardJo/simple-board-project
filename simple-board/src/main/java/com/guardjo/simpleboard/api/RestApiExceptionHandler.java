package com.guardjo.simpleboard.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

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

    @ExceptionHandler({AuthorizationServiceException.class})
    public ResponseEntity<String> handleForbidden(Exception e) {
        log.error("Error, {}", e.getMessage(), e);

        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
