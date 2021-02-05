package br.com.iagocolodetti.agenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

/**
 *
 * @author iagocolodetti
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Where(clause = "deleted = '0'")
public class Contact implements Serializable {

    private static final long serialVersionUID = 2714721884860822016L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String alias;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean deleted;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contact", orphanRemoval = true)
    @JsonIgnoreProperties("contact")
    private List<Phone> phone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contact", orphanRemoval = true)
    @JsonIgnoreProperties("contact")
    private List<Email> email;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User user;
    
    @JsonIgnore
    public void addPhone(Phone phone) {
        this.phone.add(phone);
    }
    
    @JsonIgnore
    public void addEmail(Email email) {
        this.email.add(email);
    }

    @PrePersist
    private void prePersist() {
        setDeleted(false);
        setCreatedAt(new Date());
        setUpdatedAt(new Date());
        phone.forEach(p -> p.setContact(this));
        email.forEach(e -> e.setContact(this));
    }

    @PreUpdate
    private void preUpdate() {
        setUpdatedAt(new Date());
    }
}
