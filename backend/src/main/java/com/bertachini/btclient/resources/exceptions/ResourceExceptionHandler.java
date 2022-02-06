package com.bertachini.btclient.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bertachini.btclient.services.exceptions.DataBaseException;
import com.bertachini.btclient.services.exceptions.ResourceNotFoundException;

//permite que essa classe pegue e trate excessoes 
//que acontecerem dentro do controller
@ControllerAdvice
public class ResourceExceptionHandler {
	
	//Receber a exception e cria a resposta personalizada 
	//para o controlador mostrar na requisicao
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> getEntityNotFoundException
	(ResourceNotFoundException e, HttpServletRequest request) {
		
		StandardError error = new StandardError();
		HttpStatus status = HttpStatus.NOT_FOUND;

		error.setError("Resource not found");
		error.setMessege(e.getMessage());
		error.setStatus(status.value());
		error.setPath(request.getRequestURI());
		error.setTimestamp(Instant.now());
		
		return ResponseEntity.status(status).body(error);
	} 
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> getDataBaseException(DataBaseException e, HttpServletRequest request){
		
		StandardError error = new StandardError();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		error.setError("Database exception");
		error.setMessege(e.getMessage());
		error.setStatus(status.value());
		error.setPath(request.getRequestURI());
		error.setTimestamp(Instant.now());
		
		return ResponseEntity.status(status).body(error);
		
	}

}
