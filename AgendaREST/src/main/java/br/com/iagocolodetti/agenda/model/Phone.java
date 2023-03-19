package br.com.iagocolodetti.agenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
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
@Table(name = "phone")
@Where(clause = "deleted = '0'")
public class Phone implements Serializable {

    private static final long serialVersionUID = 3814721562860822016L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "int unsigned")
    private Integer id;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, columnDefinition = "tinyint(1) default '0'")
    private boolean deleted;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedAt;
    @JoinColumn(name = "contact_id", nullable = false, columnDefinition = "int unsigned", referencedColumnName = "id")
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
