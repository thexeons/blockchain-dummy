package com.Profile.exception;

import java.util.Date;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class PersonException extends ResponseEntityExceptionHandler{
	
	//Invalid request body
	@ResponseBody
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
	  System.out.println("Exception 1");
	  ErrorDetails errorDetails = new ErrorDetails(new Date(), "Not a valid input",
	      ex.getBindingResult().getFieldError().getDefaultMessage().toString());
	  return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	} 

	//Invalid path variable type
	@ResponseBody
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
	     WebRequest request) {
		System.out.println("Exception 2");
		ErrorDetails errorDetails = new ErrorDetails(new Date(), "Mismatch type for parameter: " + ex.getName().toString(),
	      request.getDescription(false));
	  return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	//Invalid request body data type
	@ResponseBody
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("Exception 3");
		ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid request body data type", ex.getMostSpecificCause().getLocalizedMessage().toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	//Invalid delete parameter
	@ResponseBody
	@ExceptionHandler(EmptyResultDataAccessException.class)
	protected final ResponseEntity<Object> handleEmptyResultData(EmptyResultDataAccessException ex, WebRequest request){
		System.out.println("Exception 4");
		ErrorDetails errorDetails = new ErrorDetails(new Date(), "No " + ex.getLocalizedMessage().substring(ex.getLocalizedMessage().lastIndexOf('.')+1),
		        request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	//Invalid path variable/missing request body
	@ResponseBody
	@ExceptionHandler(IllegalArgumentException.class)
	protected final ResponseEntity<Object> handleNotFoundException(IllegalArgumentException ex, WebRequest request){
		System.out.println("Exception 5");
		ErrorDetails errorDetails;
		errorDetails = new ErrorDetails(new Date(), "Parameter cannot be found", ex.getLocalizedMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	//Invalid request body
	@ResponseBody
	@ExceptionHandler(NullPointerException.class)
	protected final ResponseEntity<Object> handleNullException(NullPointerException ex, WebRequest request){
		System.out.println("Exception 5b");
		ErrorDetails errorDetails;
		errorDetails = new ErrorDetails(new Date(), "Missing parameter in request body", request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	//any other exception
	@ResponseBody
	@ExceptionHandler(Exception.class)
	  protected final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		System.out.println("Exception 7");
		ErrorDetails errorDetails;
		if(ex.getMessage() != null) {
			errorDetails = new ErrorDetails(new Date(), ex.getLocalizedMessage(),
					request.getDescription(false));
		} else {
			errorDetails = new ErrorDetails(new Date(), "Please try again later",
					request.getDescription(false));
		}
	    	
	    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  
	
}
