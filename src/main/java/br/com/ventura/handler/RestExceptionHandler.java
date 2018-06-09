package br.com.ventura.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.ventura.error.ConstraintViolationDetails;
import br.com.ventura.error.ConstraintViolationException;
import br.com.ventura.error.ErrorDetails;
import br.com.ventura.error.MalformedJwtDetails;
import br.com.ventura.error.MalformedJwtException;
import br.com.ventura.error.ResourceNotFoundDetails;
import br.com.ventura.error.ResourceNotFoundException;
import br.com.ventura.error.ValidationErrorDetails;

/**
 * Classe responsável pela manipulação de exceções responsável por padronizar
 * as mensagens de erro da API
 * 
 * @author Daniel Ventura
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException) {
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .detail(rfnException.getMessage())
                .developerMessage(rfnException.getClass().getName())
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException cvException) {
    	ConstraintViolationDetails cvDetails = ConstraintViolationDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.CONFLICT.value())
                .title("Constraint Violation")
                .detail(cvException.getMessage())
                .developerMessage(cvException.getClass().getName())
                .build();
        return new ResponseEntity<>(cvDetails, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException mjException) {
        MalformedJwtDetails mjDetails = MalformedJwtDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.FORBIDDEN.value())
                .title("Forbidden")
                .detail(mjException.getMessage())
                .developerMessage(mjException.getClass().getName())
                .build();
        return new ResponseEntity<>(mjDetails, HttpStatus.FORBIDDEN);
    }
    

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException,
                                                          HttpHeaders headers,
                                                          HttpStatus status,
                                                          WebRequest request) {
        List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        ValidationErrorDetails rnfDetails = ValidationErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Field Validation Error")
                .detail("Field Validation Error")
                .developerMessage(manvException.getClass().getName())
                .field(fields)
                .fieldMessage(fieldMessages)
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value())
                .title("Internal Exception")
                .detail(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();
        return new ResponseEntity<>(errorDetails, headers, status);
    }
}
