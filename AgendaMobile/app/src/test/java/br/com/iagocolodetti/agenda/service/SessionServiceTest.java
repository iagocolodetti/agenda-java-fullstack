package br.com.iagocolodetti.agenda.service;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.service.SessionService;

import static org.junit.Assert.*;

public class SessionServiceTest {

    @Test
    public void sessionCreate() throws IOException {
        String authorization = new SessionService().create(new User("criarUsuarioTeste", "senha"));
        assertNotNull(authorization);
    }

    @Test
    public void sessionCreateFail() throws IOException {
        String authorization = null;
        int statusCode = -1;
        try {
            authorization = new SessionService().create(new User("criarUsuarioTeste", "senhaerrada"));
        } catch (CustomResponseException ex) {
            statusCode = ex.getStatus();
        } finally {
            assertNull(authorization);
            assertEquals(HttpURLConnection.HTTP_NOT_FOUND, statusCode);
        }
    }
}
