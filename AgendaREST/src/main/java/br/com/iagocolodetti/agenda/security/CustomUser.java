package br.com.iagocolodetti.agenda.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author iagocolodetti
 */
public class CustomUser extends User {

    private final int userid;

    public CustomUser(int userid, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userid = userid;
    }

    public int getUserId() {
        return userid;
    }
}
