package com.code.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.code.blog.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
		/*String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		new ResponseEntity<ApiResponse>(apiResponse, false), HttpStatus.NOT_FOUND);*/
		return new ResponseEntity<ApiResponse>(new ApiResponse(ex.getMessage(), false), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex){
		Map<String, String> responseMap = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName =  ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			responseMap.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> handleApiException(ApiException ex){
		/*String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		new ResponseEntity<ApiResponse>(apiResponse, false), HttpStatus.NOT_FOUND);*/
		return new ResponseEntity<ApiResponse>(new ApiResponse(ex.getMessage(), true), HttpStatus.BAD_REQUEST);
	}
}
