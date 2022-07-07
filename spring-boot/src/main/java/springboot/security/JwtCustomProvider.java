package springboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import springboot.configs.CustomProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtCustomProvider implements AuthenticationProvider {

    private final String securityRole;
    private final JwtUtility jwtUtility;

    public JwtCustomProvider(CustomProperties customProperties, JwtUtility jwtUtility) {
        this.securityRole = customProperties.getSecurityRole();
        this.jwtUtility = jwtUtility;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Claims claims;
        String token = (String) authentication.getDetails();
        try {
            claims = this.jwtUtility.jwtParse(token);
        } catch (JwtException jwtException) {
            throw new SecurityException("error test", jwtException);
        }
        return new JwtCustomToken(claims, token, this.createGrantedAuthorities(claims));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtCustomToken.class.isAssignableFrom(authentication);
    }

    private Collection<? extends GrantedAuthority> createGrantedAuthorities(Claims claims) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Object roles = claims.get(this.securityRole);
        if (roles instanceof ArrayList<?>) {
            for (Object role : (ArrayList<?>) roles) {
                if (role instanceof String)
                    grantedAuthorities.add(() -> (String) role);
            }
        }
        return grantedAuthorities;
    }
}
