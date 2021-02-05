package br.com.iagocolodetti.agenda.repository;

import br.com.iagocolodetti.agenda.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author iagocolodetti
 */
@Repository
public interface UserRepository extends CustomRepository<User, Integer> {
    
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);
}
