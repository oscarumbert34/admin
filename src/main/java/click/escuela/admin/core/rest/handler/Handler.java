package click.escuela.admin.core.rest.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import click.escuela.admin.core.exception.ErrorStudent;
import click.escuela.admin.core.exception.TransactionException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class Handler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(TransactionException.class)
	public ResponseEntity<String> handleTransactionException(TransactionException e) {
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorStudent> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		logger.error(e.getMessage());
		BindingResult result = e.getBindingResult();
		List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(processFieldErrors(fieldErrors));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handlerException(Exception e) {
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	
	private ErrorStudent processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
		ErrorStudent error = new ErrorStudent(BAD_REQUEST.value(), "validation error");
		for (org.springframework.validation.FieldError fieldError : fieldErrors) {
			error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return error;
	}
}