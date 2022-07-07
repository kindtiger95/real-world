package springboot.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtCustomToken extends AbstractAuthenticationToken {

    @Getter
    private String jsonWebToken;

    private String credentials;
    private Claims principal;

    public JwtCustomToken(String jsonWebToken) {
        super(null);
        this.setDetails(jsonWebToken);
        this.setAuthenticated(false);
    }

    public JwtCustomToken(Claims principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public String getCredentials() {
        return this.credentials;
    }

    @Override
    public Claims getPrincipal() {
        return this.principal;
    }
}
