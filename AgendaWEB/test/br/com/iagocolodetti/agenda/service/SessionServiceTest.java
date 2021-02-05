package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iagocolodetti
 */
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
            assertEquals(HttpStatus.SC_NOT_FOUND, statusCode);
        }
    }
}
