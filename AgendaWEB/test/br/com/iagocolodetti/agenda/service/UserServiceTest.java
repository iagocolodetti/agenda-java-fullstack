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
public class UserServiceTest {
    
    @Test
    public void userCreate() throws IOException {
        try {
            int statusCode = new UserService().create(new User("criarUsuarioTeste", "senha"));
            assertEquals(HttpStatus.SC_CREATED, statusCode);
        } catch (CustomResponseException ex) {
            assertEquals(HttpStatus.SC_CONFLICT, ex.getStatus());
        }
    }
}
