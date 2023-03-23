package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Email;
import br.com.iagocolodetti.agenda.model.Phone;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.repository.ContactRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author iagocolodetti
 */
@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Transactional
    public Contact create(Integer userid, Contact contact) throws ResponseStatusException {
        try {
            User user = new User();
            user.setId(userid);
            contact.setUser(user);
            contactRepository.save(contact);
            contactRepository.refresh(contact);
            return contact;
        } catch (NullPointerException | DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadastrar o contato");
        }
        
    }
    
    public List<Contact> read(Integer userid, Pageable pageable) {
        return contactRepository.findAllByUser(userid, pageable);
    }
    
    @Transactional
    public void update(Integer userId, Integer contactId, Contact contact) throws ResponseStatusException {
        try {
            Optional<Contact> contactOptional = contactRepository.findByUserAndId(userId, contactId);
            if (contactOptional.isPresent()) {
                Contact contactUpdate = contactOptional.get();
                contactUpdate.setName(contact.getName());
                contactUpdate.setAlias(contact.getAlias());
                contact.getPhone().forEach(np -> {
                    if (np.getId() != null) {
                        boolean exists = false;
                        for (int i = 0; i < contactUpdate.getPhone().size(); i++) {
                            Phone p = contactUpdate.getPhone().get(i);
                            if (np.getId().equals(p.getId())) {
                                p.setPhone(np.getPhone());
                                p.setDeleted(np.isDeleted());
                                contactUpdate.getPhone().set(i, p);
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            np.setId(null);
                            np.setContact(contactUpdate);
                            contactUpdate.addPhone(np);
                        }
                    } else {
                        np.setContact(contactUpdate);
                        contactUpdate.addPhone(np);
                    }
                });
                contact.getEmail().forEach(ne -> {
                    if (ne.getId() != null) {
                        boolean exists = false;
                        for (int i = 0; i < contactUpdate.getEmail().size(); i++) {
                            Email e = contactUpdate.getEmail().get(i);
                            if (ne.getId().equals(e.getId())) {
                                e.setEmail(ne.getEmail());
                                e.setDeleted(ne.isDeleted());
                                contactUpdate.getEmail().set(i, e);
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            ne.setId(null);
                            ne.setContact(contactUpdate);
                            contactUpdate.addEmail(ne);
                        }
                    } else {
                        ne.setContact(contactUpdate);
                        contactUpdate.addEmail(ne);
                    }
                });
                contactRepository.save(contactUpdate);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
            }
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível atualizar o contato");
        }
    }
    
    @Transactional
    public void logicalDelete(Integer userId, Integer contactId) throws ResponseStatusException {
        try {
            Optional<Contact> contactOptional = contactRepository.findByUserAndId(userId, contactId);
            if (contactOptional.isPresent()) {
                contactOptional.get().setDeleted(true);
                contactRepository.save(contactOptional.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
            }
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível excluir o contato");
        }
    }
}
