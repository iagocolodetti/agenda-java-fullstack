package br.com.iagocolodetti.agenda.model;

/**
 *
 * @author iagocolodetti
 */
public class Email {

    private Integer id;
    private String email;
    private boolean deleted = false;

    public Email() {
    }

    public Email(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
