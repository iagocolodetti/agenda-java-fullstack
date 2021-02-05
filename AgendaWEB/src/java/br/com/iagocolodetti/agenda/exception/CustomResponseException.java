package br.com.iagocolodetti.agenda.exception;

import java.io.IOException;

/**
 *
 * @author iagocolodetti
 */
public class CustomResponseException extends IOException {
    
    private int status;
    
    public CustomResponseException() {
        super();
    }

    public CustomResponseException(String message) {
        super(message);
    }
    
    public CustomResponseException(String message, int status) {
        super(message);
        this.status = status;
    }
    
    public int getStatus() {
        return status;
    }
}
