package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.service.ContactService;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import org.apache.hc.core5.http.HttpStatus;

/**
 *
 * @author iagocolodetti
 */
public class ContactServlet extends HttpServlet {
    
    private void sendError(HttpServletResponse response, String error, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter out = response.getWriter()) {
            out.println("{\"error\":\"" + error + "\",\"status\":\"" + status + "\"}");
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getSession().isNew()) {
            Object contactJson = request.getSession().getAttribute("contactUpdate");
            if (contactJson != null) {
                request.getSession().removeAttribute("contactUpdate");
                response.setContentType(MediaType.APPLICATION_JSON);
                String contactJsonDecoded = new String(contactJson.toString().getBytes(), StandardCharsets.UTF_8);
                try (PrintWriter out = response.getWriter()) {
                    out.println(contactJsonDecoded);
                    out.close();
                }
            }
        } else {
            AuthUtils.forceLogout(request, response, "Sessão expirada, faça o login novamente.", HttpStatus.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ContactService cs = new ContactService();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        Contact contact = new Contact(sb.toString());
        contact.getPhone().forEach(phone -> {
            if (phone.getId() < 0) {
                phone.setId(null);
            }
        });
        contact.getEmail().forEach(email -> {
            if (email.getId() < 0) {
                email.setId(null);
            }
        });
        if (contact.getId() == null) {
            try {
                cs.create(AuthUtils.getAuth(request), contact);
            } catch (CustomResponseException ex) {
                if (ex.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                    AuthUtils.forceLogout(request, response, "Erro: " + ex.getMessage() + ".", ex.getStatus());
                } else {
                    sendError(response, ex.getMessage(), ex.getStatus());
                }
            }
        } else {
            try {
                cs.update(AuthUtils.getAuth(request), contact);
            } catch (CustomResponseException ex) {
                if (ex.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                    AuthUtils.forceLogout(request, response, "Erro: " + ex.getMessage() + ".", ex.getStatus());
                } else {
                    sendError(response, ex.getMessage(), ex.getStatus());
                }
            }
        }
    }
}
