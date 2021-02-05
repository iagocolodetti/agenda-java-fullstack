package br.com.iagocolodetti.agenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Phone implements Serializable {

    private static final long serialVersionUID = 3814721562860822016L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phone;
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
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @ManyToOne
    private Contact contact;

    @PrePersist
    private void prePersist() {
        setDeleted(false);
        setCreatedAt(new Date());
        setUpdatedAt(new Date());
    }

    @PreUpdate
    private void preUpdate() {
        setUpdatedAt(new Date());
    }
}
