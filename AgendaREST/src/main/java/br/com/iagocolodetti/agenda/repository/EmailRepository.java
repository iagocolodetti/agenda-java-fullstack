package br.com.iagocolodetti.agenda.repository;

import br.com.iagocolodetti.agenda.model.Email;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author iagocolodetti
 */
public interface EmailRepository extends CustomRepository<Email, Integer> {
    
    @Modifying
    @Query(value = "DELETE FROM Email", nativeQuery = true)
    @Override
    public void deleteAll();
}
