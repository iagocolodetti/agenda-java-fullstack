package br.com.iagocolodetti.agenda.error;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 *
 * @author iagocolodetti
 */
public class CustomResponseJsonError {
    
    public CustomResponseJsonError(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(new CustomJsonError(request, status, message));
        response.getWriter().flush();
    }
}
