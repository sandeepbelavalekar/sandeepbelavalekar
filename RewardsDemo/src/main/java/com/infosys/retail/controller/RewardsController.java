package com.infosys.retail.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.retail.dto.CustomerDTO;
import com.infosys.retail.dto.PurchaseDTO;
import com.infosys.retail.dto.ResponseData;
import com.infosys.retail.service.RewardsService;

import jakarta.validation.Valid;

@RestController

public class RewardsController {

	@Autowired
	RewardsService service;
	
	@PostMapping("/registerCustomer")
	public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customer) {
		ResponseEntity<String> responseEntity = null;
		ResponseData responseData = service.registerCustomer(customer);
		if (responseData.isStatus()) {
			responseEntity = new ResponseEntity<String>(responseData.getMsg(), HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<String>(responseData.getMsg(), HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}

	@PostMapping("/purchaseItems")
	public ResponseEntity<String> purchaseItems(@Valid @RequestBody PurchaseDTO p) {
		ResponseData responseData = service.purchaseItem(p);
		ResponseEntity<String> responseEntity = null;

		if (responseData.isStatus()) {
			responseEntity = new ResponseEntity<String>(responseData.getMsg(), HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<String>(responseData.getMsg(), HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}
	
	@GetMapping("/checkBalance")
	public ResponseEntity<ResponseData> getBalance(@RequestParam(required = false)String name,@RequestParam String phoneNumber) {
		
		ResponseData responseData = service.getBalance(phoneNumber);
		
		ResponseEntity<ResponseData> responseEntity = new ResponseEntity<ResponseData>(responseData, HttpStatus.OK);
		return responseEntity;
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}	

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace();
		return new ResponseEntity<>("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
