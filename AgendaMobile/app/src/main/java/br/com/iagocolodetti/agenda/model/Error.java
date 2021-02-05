package br.com.iagocolodetti.agenda.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;

/**
 *
 * @author iagocolodetti
 */
public class Error {

    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String method;

    public Error() {
    }

    public Error(String json) {
        try {
            Error error = new ObjectMapper().readValue(json, Error.class);
            this.timestamp = error.getTimestamp();
            this.status = error.getStatus();
            this.error = error.getError();
            this.message = error.getMessage();
            this.path = error.getPath();
            this.method = error.getMethod();
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }
}
