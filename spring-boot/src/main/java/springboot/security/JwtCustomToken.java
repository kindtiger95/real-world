package springboot.security;

import java.util.Collection;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtCustomToken extends AbstractAuthenticationToken {

    @Getter
    private String jsonWebToken;

    private String credentials;
    private String principal;

    public JwtCustomToken(String jsonWebToken) {
        super(null);
        this.jsonWebToken = jsonWebToken;
        this.setAuthenticated(false);
    }

    public JwtCustomToken(
        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public String getCredentials() {
        return this.credentials;
    }

    @Override
    public String getPrincipal() {
        return this.principal;
    }
}
