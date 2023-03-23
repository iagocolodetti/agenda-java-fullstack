package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.repository.UserRepository;
import br.com.iagocolodetti.agenda.security.CustomUser;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author iagocolodetti
 */
@Service
public class SessionService {
    
    @Autowired
    private UserRepository userRepository;
    
    public UserDetails findByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new CustomUser(user.getId(), user.getUsername(), user.getPassword(), Collections.emptyList());
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
