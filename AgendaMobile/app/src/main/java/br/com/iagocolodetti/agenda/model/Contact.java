package br.com.iagocolodetti.agenda.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author iagocolodetti
 */
public class Contact {

    private Integer id;
    private String name;
    private String alias;
    private boolean deleted = false;
    private List<Phone> phone;
    private List<Email> email;

    public Contact() {
    }

    public Contact(String json) {
        try {
            Contact contact = new ObjectMapper().readValue(json, Contact.class);
            this.id = contact.getId();
            this.name = contact.getName();
            this.alias = contact.getAlias();
            this.deleted = contact.isDeleted();
            this.phone = contact.getPhone();
            this.email = contact.getEmail();
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    public Contact(String name, String alias, List<Phone> phone, List<Email> email) {
        this.name = name;
        this.alias = alias;
        this.phone = phone;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public List<Email> getEmail() {
        return email;
    }

    public void setEmail(List<Email> email) {
        this.email = email;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }
}
