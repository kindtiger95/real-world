package springboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import springboot.CustomProperties;

import java.lang.reflect.Array;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtCustomProvider implements AuthenticationProvider {

    private final String ROLES;
    private final JwtParser jwtParser;

    public JwtCustomProvider(CustomProperties customProperties) {
        this.ROLES = customProperties.getJwtRoles();
        byte[] secretKeyByte = customProperties.getJwtSecretKey().getBytes();
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKeyByte).build();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Claims claims;
        try {
            claims = this.jwtParser.parseClaimsJws((String) authentication.getDetails()).getBody();
        } catch (JwtException jwtException) {
            throw new SecurityException("error test", jwtException);
        }
        return new JwtCustomToken(claims.getSubject(), "", this.createGrantedAuthorities(claims));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtCustomToken.class.isAssignableFrom(authentication);
    }

    private Collection<? extends GrantedAuthority> createGrantedAuthorities(Claims claims) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Object roles = claims.get(ROLES);
        if (roles instanceof ArrayList<?>) {
            for (Object role : (ArrayList<?>) roles) {
                if (role instanceof String)
                    grantedAuthorities.add(() -> (String) role);
            }
        }
        return grantedAuthorities;
    }
}
