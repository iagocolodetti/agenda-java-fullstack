package br.com.iagocolodetti.agenda.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author iagocolodetti
 */
public class User {

    private String username;
    private String password;

    public User() {
    }

    public User(String json) {
        try {
            User user = new ObjectMapper().readValue(json, User.class);
            this.username = user.getUsername();
            this.password = user.getPassword();
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }
}
