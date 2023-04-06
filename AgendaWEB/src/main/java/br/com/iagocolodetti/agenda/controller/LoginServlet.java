package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.service.SessionService;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.core5.http.HttpStatus;

public class LoginServlet extends HttpServlet {
    
    private void sendError(HttpServletRequest request, HttpServletResponse response, String error, Integer status)
            throws ServletException, IOException {
        request.setAttribute("message", "Erro: " + error);
        request.setAttribute("alert", "alert-danger");
        if (status != null) {
            response.setStatus(status);
        }
        request.getRequestDispatcher("pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!response.isCommitted()) {
            if (AuthUtils.getAuth(request).isEmpty()) {
                request.getRequestDispatcher("pages/login.jsp").forward(request, response);
            } else {
                response.sendRedirect("contacts");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        request.setAttribute("username", username);
        if (username.isEmpty()) {
            sendError(request, response, "Preencha o campo destinado ao nome.", HttpStatus.SC_BAD_REQUEST);
        } else if (password.isEmpty()) {
            sendError(request, response, "Preencha o campo destinado à senha.", HttpStatus.SC_BAD_REQUEST);
        } else {
            SessionService ss = new SessionService();
            try {
                String auth = ss.create(new User(username, password));
                AuthUtils.setAuth(request, auth);
                response.sendRedirect("contacts");
            } catch (CustomResponseException ex) {
                sendError(request, response, ex.getMessage(), ex.getStatus());
            } catch (HttpHostConnectException ex) {
                sendError(request, response, "Não foi possível realizar o login.", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}
