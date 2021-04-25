package br.com.iagocolodetti.agenda.repository;

import br.com.iagocolodetti.agenda.model.Phone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author iagocolodetti
 */
public interface PhoneRepository extends CustomRepository<Phone, Integer> {
    
    @Modifying
    @Query(value = "DELETE FROM Phone", nativeQuery = true)
    @Override
    public void deleteAll();
}
