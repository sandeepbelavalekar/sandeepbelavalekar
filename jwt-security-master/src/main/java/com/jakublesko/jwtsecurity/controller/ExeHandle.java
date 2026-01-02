package com.jakublesko.jwtsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExeHandle extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleexp(Exception exe, WebRequest req) {
		
		exe.printStackTrace();
		
		return new ResponseEntity<String> ("authentication failsed",HttpStatus.INTERNAL_SERVER_ERROR);
		
		
	}

}
