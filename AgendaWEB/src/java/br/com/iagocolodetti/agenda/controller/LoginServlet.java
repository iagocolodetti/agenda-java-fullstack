package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.service.SessionService;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

public class LoginServlet extends HttpServlet {

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
            request.setAttribute("message", "Erro: Preencha o campo destinado ao nome.");
            request.setAttribute("alert", "alert-danger");
            request.getRequestDispatcher("pages/login.jsp").forward(request, response);
        } else if (password.isEmpty()) {
            request.setAttribute("message", "Erro: Preencha o campo destinado à senha.");
            request.setAttribute("alert", "alert-danger");
            request.getRequestDispatcher("pages/login.jsp").forward(request, response);
        } else {
            SessionService ss = new SessionService();
            try {
                String auth = ss.create(new User(username, password));
                AuthUtils.setAuth(request, auth);
                response.sendRedirect("contacts");
            } catch (CustomResponseException ex) {
                request.setAttribute("message", "Erro: " + ex.getMessage());
                request.setAttribute("alert", "alert-danger");
                response.setStatus(ex.getStatus());
                request.getRequestDispatcher("pages/login.jsp").forward(request, response);
            } catch (HttpHostConnectException ex) {
                request.setAttribute("message", "Erro: Não foi possível realizar o login.");
                request.setAttribute("alert", "alert-danger");
                response.setStatus(500);
                request.getRequestDispatcher("pages/login.jsp").forward(request, response);
            }
        }
    }
}
