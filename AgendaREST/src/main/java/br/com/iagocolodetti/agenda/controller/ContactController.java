package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.error.CustomJsonError;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Email;
import br.com.iagocolodetti.agenda.model.Phone;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.repository.ContactRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author iagocolodetti
 */
@RestController
@RequestMapping({"/contacts"})
public class ContactController {

    @Autowired
    private ContactRepository repository;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody Contact contact) {
        try {
            User user = new User();
            user.setId(Integer.parseInt((String) request.getAttribute("userid")));
            contact.setUser(user);
            repository.save(contact);
            repository.refresh(contact);
            return new ResponseEntity<>(contact, HttpStatus.CREATED);
        } catch (NullPointerException | DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomJsonError(request, HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadastrar o contato").toString());
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> read(HttpServletRequest request, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        try {
            Pageable pageable;
            if (page == null || page < 0) {
                pageable = Pageable.unpaged();
            } else {
                pageable = PageRequest.of(page, ((size == null || size < 1) ? 10 : size));
            }
            return new ResponseEntity<>(
                    repository.findAllByUser(Integer.parseInt((String) request.getAttribute("userid")), pageable),
                    HttpStatus.OK);
        } catch (NumberFormatException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomJsonError(request, HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível buscar os contatos").toString());
        }
    }

    @PutMapping(path = {"/{id}"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable Integer id, @RequestBody Contact contact) {
        try {
            return repository.findByUserAndId(Integer.parseInt((String) request.getAttribute("userid")), id)
                    .map(result -> {
                        result.setName(contact.getName());
                        result.setAlias(contact.getAlias());
                        contact.getPhone().forEach(np -> {
                            if (np.getId() != null) {
                                boolean exists = false;
                                for (int i = 0; i < result.getPhone().size(); i++) {
                                    Phone p = result.getPhone().get(i);
                                    if (np.getId().equals(p.getId())) {
                                        p.setPhone(np.getPhone());
                                        p.setDeleted(np.isDeleted());
                                        result.getPhone().set(i, p);
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    np.setId(null);
                                    np.setContact(result);
                                    result.addPhone(np);
                                }
                            } else {
                                np.setContact(result);
                                result.addPhone(np);
                            }
                        });
                        contact.getEmail().forEach(ne -> {
                            if (ne.getId() != null) {
                                boolean exists = false;
                                for (int i = 0; i < result.getEmail().size(); i++) {
                                    Email e = result.getEmail().get(i);
                                    if (ne.getId().equals(e.getId())) {
                                        e.setEmail(ne.getEmail());
                                        e.setDeleted(ne.isDeleted());
                                        result.getEmail().set(i, e);
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    ne.setId(null);
                                    ne.setContact(result);
                                    result.addEmail(ne);
                                }
                            } else {
                                ne.setContact(result);
                                result.addEmail(ne);
                            }
                        });
                        repository.save(result);
                        return ResponseEntity.noContent().build();
                    }).orElse(ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CustomJsonError(request, HttpStatus.NOT_FOUND, "Contato não encontrado").toString())
            );
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomJsonError(request, HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível atualizar o contato").toString());
        }
    }

    @DeleteMapping(path = {"/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> destroy(HttpServletRequest request, @PathVariable Integer id) {
        try {
            return repository.findByUserAndId(Integer.parseInt((String) request.getAttribute("userid")), id)
                    .map(result -> {
                        result.setDeleted(true);
                        repository.save(result);
                        return ResponseEntity.noContent().build();
                    }).orElse(ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CustomJsonError(request, HttpStatus.NOT_FOUND, "Contato não encontrado").toString())
            );
        } catch (NumberFormatException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomJsonError(request, HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível excluir o contato").toString());
        }
    }
}
