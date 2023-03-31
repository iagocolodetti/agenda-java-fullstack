package br.com.iagocolodetti.agenda.exception;

import br.com.iagocolodetti.agenda.error.CustomJsonError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author iagocolodetti
 */
@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CustomJsonError(request, HttpStatus.BAD_REQUEST, ex.getAllErrors().get(0).getDefaultMessage()).toString());
        
    }
}
