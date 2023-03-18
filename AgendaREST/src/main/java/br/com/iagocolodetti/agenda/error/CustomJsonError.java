package br.com.iagocolodetti.agenda.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.WordUtils;
import org.springframework.http.HttpStatus;

/**
 *
 * @author iagocolodetti
 */
public class CustomJsonError {
    
    private final Map<String, String> linkedHashMap;
    
    public CustomJsonError(HttpServletRequest request, HttpStatus status, String message) {
        linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        linkedHashMap.put("status", String.valueOf(status.value()));
        linkedHashMap.put("error", WordUtils.capitalizeFully(status.name().replace("_", " ")));
        linkedHashMap.put("message", message);
        linkedHashMap.put("path", request.getServletPath());
        linkedHashMap.put("method", request.getMethod());
    }
    
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(linkedHashMap);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }
}
