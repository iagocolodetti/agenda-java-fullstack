package br.com.iagocolodetti.agenda.model;

/**
 *
 * @author iagocolodetti
 */
public class Phone {

    private Integer id;
    private String phone;
    private boolean deleted = false;

    public Phone() {
    }

    public Phone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
