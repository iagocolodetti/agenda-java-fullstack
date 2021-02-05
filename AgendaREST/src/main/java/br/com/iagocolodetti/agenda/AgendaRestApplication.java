package br.com.iagocolodetti.agenda;

import br.com.iagocolodetti.agenda.repository.CustomRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author iagocolodetti
 */
@SpringBootApplication
@EnableJpaRepositories (repositoryBaseClass = CustomRepositoryImpl.class)
public class AgendaRestApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AgendaRestApplication.class, args);
    }
}
