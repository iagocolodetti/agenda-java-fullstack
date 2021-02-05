package br.com.iagocolodetti.agenda.security;

import br.com.iagocolodetti.agenda.error.CustomResponseJsonError;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author iagocolodetti
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            new CustomResponseJsonError(
                    (HttpServletRequest) request,
                    (HttpServletResponse) response,
                    ex.getStatus(),
                    ex.getMessage()
            );
        }
    }
}
