package br.com.ventura.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Daniel Ventura
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class MalformedJwtException extends RuntimeException {
	

	private static final long serialVersionUID = 3017902720396629711L;

	public MalformedJwtException(String message) {
        super(message);
    }
}
