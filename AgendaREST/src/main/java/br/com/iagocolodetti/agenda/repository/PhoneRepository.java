package br.com.iagocolodetti.agenda.repository;

import br.com.iagocolodetti.agenda.model.Phone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author iagocolodetti
 */
@Repository
public interface PhoneRepository extends CustomRepository<Phone, Integer> {
    
    @Modifying
    @Query(value = "DELETE FROM Phone", nativeQuery = true)
    @Override
    public void deleteAll();
}
