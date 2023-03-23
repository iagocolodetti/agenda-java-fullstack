package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author iagocolodetti
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
            
    @Transactional
    public User save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }
}
