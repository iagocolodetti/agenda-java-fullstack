package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.error.CustomJsonError;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author iagocolodetti
 */
@RestController
@RequestMapping({"/users"})
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody User user) {
        try {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            return new ResponseEntity<>(repository.save(user), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new CustomJsonError(request, HttpStatus.CONFLICT, "Usuário já existe").toString());
        }
    }
}
