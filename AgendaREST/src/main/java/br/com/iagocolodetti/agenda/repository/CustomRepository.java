package br.com.iagocolodetti.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;

/**
 *
 * @author iagocolodetti
 */
@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    
    void refresh(T t);
}
