package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.dto.UserDto;
import br.com.iagocolodetti.agenda.error.CustomJsonError;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private UserService userService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody @Valid UserDto userDto) {
        try {
            User user = modelMapper.map(userDto, User.class);
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new CustomJsonError(request, HttpStatus.CONFLICT, "Usuário já existe").toString());
        }
    }
}
