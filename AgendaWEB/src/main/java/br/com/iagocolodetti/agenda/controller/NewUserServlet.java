package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import br.com.iagocolodetti.agenda.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.hc.core5.http.HttpStatus;

/**
 *
 * @author iagocolodetti
 */
public class NewUserServlet extends HttpServlet {
    
    private final int USERNAME_MIN_LENGTH = 3;
    private final int USERNAME_MAX_LENGTH = 45;
    private final int PASSWORD_MIN_LENGTH = 5;
    private final int PASSWORD_MAX_LENGTH = 60;
    
    private void sendError(HttpServletRequest request, HttpServletResponse response, String error, int status)
            throws ServletException, IOException {
        request.setAttribute("message", "Erro: " + error);
        request.setAttribute("alert", "alert-danger");
        response.setStatus(status);
        request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!response.isCommitted()) {
            if (AuthUtils.getAuth(request).isEmpty()) {
                request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
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
        String passwordConfirm = request.getParameter("passwordConfirm");
        request.setAttribute("username", username);
        if (username.isEmpty()) {
            sendError(request, response, "Preencha o campo destinado ao nome.", HttpStatus.SC_BAD_REQUEST);
        } else if (username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH) {
            sendError(request, response, "O nome deve possuir de " + USERNAME_MIN_LENGTH + " à " + USERNAME_MAX_LENGTH + " caracteres.", HttpStatus.SC_BAD_REQUEST);
        } else if (password.isEmpty()) {
            sendError(request, response, "Preencha o campo destinado à senha.", HttpStatus.SC_BAD_REQUEST);
        } else if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            sendError(request, response, "A senha deve possuir de " + PASSWORD_MIN_LENGTH + " à " + PASSWORD_MAX_LENGTH + " caracteres.", HttpStatus.SC_BAD_REQUEST);
        } else if (passwordConfirm.isEmpty()) {
            sendError(request, response, "Confirme a senha.", HttpStatus.SC_BAD_REQUEST);
        } else if (!password.equals(passwordConfirm)) {
            sendError(request, response, "Senhas diferentes.", HttpStatus.SC_BAD_REQUEST);
        } else {
            UserService us = new UserService();
            try {
                us.create(new User(username, password));
                request.removeAttribute("username");
                request.setAttribute("message", "Usuário " + username + " cadastrado com sucesso.");
                request.setAttribute("alert", "alert-success");
                request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
            } catch (CustomResponseException ex) {
                sendError(request, response, ex.getMessage(), ex.getStatus());
            }
        }
    }
}
