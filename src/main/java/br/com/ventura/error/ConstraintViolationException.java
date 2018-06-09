package br.com.ventura.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniel Ventura
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConstraintViolationException extends RuntimeException {

	private static final long serialVersionUID = -1011530966060645651L;

	public ConstraintViolationException(String message) {
        super(message);
    }
}
