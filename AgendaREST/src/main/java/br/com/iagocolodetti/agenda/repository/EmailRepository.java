package br.com.iagocolodetti.agenda.repository;

import br.com.iagocolodetti.agenda.model.Email;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author iagocolodetti
 */
@Repository
public interface EmailRepository extends CustomRepository<Email, Integer> {
    
    @Modifying
    @Query(value = "DELETE FROM Email", nativeQuery = true)
    @Override
    public void deleteAll();
}
