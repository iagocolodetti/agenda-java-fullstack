package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.service.ContactService;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import org.apache.hc.core5.http.HttpStatus;

/**
 *
 * @author iagocolodetti
 */
public class MainServlet extends HttpServlet {

    private static List<Contact> contacts = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!response.isCommitted()) {
            String auth = AuthUtils.getAuth(request);
            if (!auth.isEmpty()) {
                try {
                    contacts = new ContactService().read(auth);
                    contacts.sort(Comparator.comparing(c -> c.getName().toUpperCase()));
                    request.setAttribute("contacts", contacts);
                    request.getRequestDispatcher("pages/main.jsp").forward(request, response);
                } catch (CustomResponseException ex) {
                    if (ex.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                        AuthUtils.forceLogout(request, response, "Erro: " + ex.getMessage() + ".", ex.getStatus());
                    } else {
                        request.setAttribute("message", "Erro: " + ex.getMessage() + ".");
                        request.setAttribute("alert", "alert-danger");
                        response.setStatus(ex.getStatus());
                        request.getRequestDispatcher("pages/main.jsp").forward(request, response);
                    }
                }
            } else {
                response.sendRedirect("login");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("update") != null) {
            request.getSession().setAttribute("contactUpdate", request.getParameter("update"));
            request.setAttribute("contacts", contacts);
            request.getRequestDispatcher("pages/main.jsp").forward(request, response);
        } else if (request.getParameter("delete") != null) {
            try {
                int id = Integer.parseInt(request.getParameter("delete"));
                new ContactService().destroy(AuthUtils.getAuth(request), id);
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getId() == id) {
                        contacts.remove(i);
                        break;
                    }
                }
            } catch (NumberFormatException ex) {
                request.setAttribute("message", "Erro: Erro interno.");
                request.setAttribute("alert", "alert-danger");
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            } catch (CustomResponseException ex) {
                if (ex.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                    AuthUtils.forceLogout(request, response, "Erro: " + ex.getMessage() + ".", ex.getStatus());
                } else {
                    request.setAttribute("message", "Erro: " + ex.getMessage() + ".");
                    request.setAttribute("alert", "alert-danger");
                    response.setStatus(ex.getStatus());
                }
            } finally {
                request.setAttribute("contacts", contacts);
                request.getRequestDispatcher("pages/main.jsp").forward(request, response);
            }
        }
    }
}
