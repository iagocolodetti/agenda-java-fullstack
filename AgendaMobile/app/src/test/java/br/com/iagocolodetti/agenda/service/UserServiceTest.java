package br.com.iagocolodetti.agenda.service;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;

import static org.junit.Assert.*;

public class UserServiceTest {

    @Test
    public void userCreate() throws IOException {
        try {
            int statusCode = new UserService().create(new User("criarUsuarioTeste", "senha"));
            assertEquals(HttpURLConnection.HTTP_CREATED, statusCode);
        } catch (CustomResponseException ex) {
            assertEquals(HttpURLConnection.HTTP_CONFLICT, ex.getStatus());
        }
    }
}
