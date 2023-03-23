package br.com.iagocolodetti.agenda.security;

import br.com.iagocolodetti.agenda.service.SessionService;
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
    private SessionService sessionService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return sessionService.findByUsername(username);
    }
}
