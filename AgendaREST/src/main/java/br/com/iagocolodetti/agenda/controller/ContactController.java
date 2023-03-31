package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.dto.ContactDto;
import br.com.iagocolodetti.agenda.error.CustomJsonError;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author iagocolodetti
 */
@RestController
@RequestMapping({"/contacts"})
public class ContactController {

    @Autowired
    private ContactService contactService;
    
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody @Valid ContactDto contactDto) {
        try {
            Contact contact = modelMapper.map(contactDto, Contact.class);
            return new ResponseEntity<>(contactService.create(Integer.parseInt((String) request.getAttribute("userid")), contact), HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(new CustomJsonError(request, (HttpStatus) ex.getStatusCode(), ex.getReason()).toString());
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
                    contactService.read(Integer.parseInt((String) request.getAttribute("userid")), pageable),
                    HttpStatus.OK);
        } catch (NumberFormatException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomJsonError(request, HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível buscar os contatos").toString());
        }
    }

    @PutMapping(path = {"/{id}"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable Integer id, @RequestBody @Valid ContactDto contactDto) {
        try {
            Contact contact = modelMapper.map(contactDto, Contact.class);
            contactService.update(Integer.parseInt((String) request.getAttribute("userid")), id, contact);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(new CustomJsonError(request, (HttpStatus) ex.getStatusCode(), ex.getReason()).toString());
        }
    }

    @DeleteMapping(path = {"/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> destroy(HttpServletRequest request, @PathVariable Integer id) {
        try {
            contactService.logicalDelete(Integer.parseInt((String) request.getAttribute("userid")), id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(new CustomJsonError(request, (HttpStatus) ex.getStatusCode(), ex.getReason()).toString());
        }
    }
}
