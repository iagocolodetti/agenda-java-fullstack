package br.com.iagocolodetti.agenda.security;

import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.repository.UserRepository;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author iagocolodetti
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user != null) {
            return new CustomUser(user.getId(), user.getUsername(), user.getPassword(), Collections.emptyList());
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
