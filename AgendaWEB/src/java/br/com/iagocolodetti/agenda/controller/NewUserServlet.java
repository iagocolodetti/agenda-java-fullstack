package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import br.com.iagocolodetti.agenda.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.ClientProtocolException;

/**
 *
 * @author iagocolodetti
 */
public class NewUserServlet extends HttpServlet {

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
            request.setAttribute("message", "Erro: Preencha o campo destinado ao nome.");
            request.setAttribute("alert", "alert-danger");
            request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
        } else if (password.isEmpty()) {
            request.setAttribute("message", "Erro: Preencha o campo destinado à senha.");
            request.setAttribute("alert", "alert-danger");
            request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
        } else if (passwordConfirm.isEmpty()) {
            request.setAttribute("message", "Erro: Confirme a senha.");
            request.setAttribute("alert", "alert-danger");
            request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
        } else if (!password.equals(passwordConfirm)) {
            request.setAttribute("message", "Erro: Senhas diferentes.");
            request.setAttribute("alert", "alert-danger");
            request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
        } else {
            UserService us = new UserService();
            try {
                us.create(new User(username, password));
                request.removeAttribute("username");
                request.setAttribute("message", "Usuário " + username + " cadastrado com sucesso.");
                request.setAttribute("alert", "alert-success");
                request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
            } catch (CustomResponseException ex) {
                request.setAttribute("message", "Erro: " + ex.getMessage());
                request.setAttribute("alert", "alert-danger");
                response.setStatus(ex.getStatus());
                request.getRequestDispatcher("pages/new-user.jsp").forward(request, response);
            }
        }
    }
}
