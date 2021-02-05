package br.com.iagocolodetti.agenda.service;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Email;
import br.com.iagocolodetti.agenda.model.Phone;
import br.com.iagocolodetti.agenda.model.User;

import static org.junit.Assert.*;

public class ContactServiceTest {

    private String authorization = null;
    private final Contact CONTACT_MOCK;

    public ContactServiceTest() {
        List<Phone> phone = new ArrayList<Phone>() {
            {
                add(new Phone("1111-1111"));
                add(new Phone("2222-2222"));
            }
        };
        List<Email> email = new ArrayList<Email>() {
            {
                add(new Email("email@gmail.com"));
                add(new Email("email@hotmail.com"));
            }
        };
        CONTACT_MOCK = new Contact("Name1", "Alias1", phone, email);
    }

    @Before
    public void createUserIfNotExists() throws IOException {
        try {
            new UserService().create(new User("criarUsuarioTeste", "senha"));
        } catch (CustomResponseException ex) {
        }
    }

    @Before
    public void createSession() throws IOException, CustomResponseException {
        authorization = new SessionService().create(new User("criarUsuarioTeste", "senha"));
    }

    @Test
    public void contactCreate() throws IOException, CustomResponseException {
        Contact contact = new ContactService().create(authorization, CONTACT_MOCK);
        assertNotNull(contact);
    }

    @Test
    public void contactsRead() throws IOException, CustomResponseException {
        List<Contact> contacts = new ContactService().read(authorization);
        assertFalse(contacts.isEmpty());
    }

    @Test
    public void contactUpdate() throws IOException, CustomResponseException {
        Contact contact = new ContactService().create(authorization, CONTACT_MOCK);
        contact.setAlias("Alias2");
        int statusCode = new ContactService().update(authorization, contact);
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, statusCode);
    }

    @Test
    public void contactDestroy() throws IOException, CustomResponseException {
        Contact contact = new ContactService().create(authorization, CONTACT_MOCK);
        int statusCode = new ContactService().destroy(authorization, contact.getId());
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, statusCode);
    }
}
