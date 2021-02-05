package br.com.iagocolodetti.agenda.security;

import org.springframework.http.HttpStatus;

/**
 *
 * @author iagocolodetti
 */
public class AuthenticationException extends Exception {
    
    private final HttpStatus status;
    
    public AuthenticationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
    
}
