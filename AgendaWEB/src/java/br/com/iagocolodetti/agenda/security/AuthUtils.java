package br.com.iagocolodetti.agenda.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author iagocolodetti
 */
public class AuthUtils {

    private static final String AUTH = "agendaApiAuth";

    public static void setAuth(HttpServletRequest request, String auth) {
        request.getSession().setAttribute(AUTH, auth);
    }

    public static String getAuth(HttpServletRequest request) {
        Object authKey = request.getSession().getAttribute(AUTH);
        return (authKey == null ? "" : authKey.toString());
    }
    
    public static void forceLogout(HttpServletRequest request, HttpServletResponse response, String error, int status)
            throws ServletException, IOException {
        request.getSession().invalidate();
        request.setAttribute("message", error);
        request.setAttribute("alert", "alert-danger");
        response.setStatus(status);
        request.getRequestDispatcher("pages/login.jsp").forward(request, response);
    }
}
