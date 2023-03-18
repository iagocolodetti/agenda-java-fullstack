package br.com.iagocolodetti.agenda.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;

/**
 *
 * @author iagocolodetti
 */
public class TokenAuthenticationService {

    private static final long EXPIRATION = 1000 * 60 * 60;
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static void addAuthentication(HttpServletResponse response, int userid, String username) {
        String JWT = Jwts.builder()
                .setIssuer("agenda")
                .setId(String.valueOf(userid))
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey)
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
        response.setDateHeader("Expires", new Date(System.currentTimeMillis() + EXPIRATION).getTime());
    }

    public static Authentication getAuthentication(HttpServletRequest request) throws AuthenticationException {
        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "").trim())
                    .getBody();
            String userid = body.getId();
            String username = body.getSubject();
            if (username != null) {
                request.setAttribute("userid", userid);
                return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            } else {
                return null;
            }
        } catch (NullPointerException ex) {
            throw new AuthenticationException(HttpStatus.BAD_REQUEST, "Token de autenticação não informado no cabeçalho");
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Token de autenticação expirado");
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | DecodingException ex) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Token de autenticação inválido");
        }
    }
}
