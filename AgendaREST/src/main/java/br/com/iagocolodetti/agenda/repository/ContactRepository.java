package br.com.iagocolodetti.agenda.repository;

import br.com.iagocolodetti.agenda.model.Contact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author iagocolodetti
 */
public interface ContactRepository extends CustomRepository<Contact, Integer> {
    
    @Query("SELECT c FROM Contact c WHERE c.user.id = ?1")
    List<Contact> findAllByUser(Integer userid);
    
    @Query("SELECT c FROM Contact c WHERE c.user.id = ?1 AND c.id = ?2")
    Optional<Contact> findByUserAndId(Integer userid, Integer id);
}
