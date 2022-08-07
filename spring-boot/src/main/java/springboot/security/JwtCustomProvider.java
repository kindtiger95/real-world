package springboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import springboot.configs.CustomProperties;
import springboot.database.repositories.UserRepository;

@Component
public class JwtCustomProvider implements AuthenticationProvider {

    private final String securityRole;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;

    public JwtCustomProvider(CustomProperties customProperties, JwtUtility jwtUtility, UserRepository userRepository) {
        this.securityRole = customProperties.getSecurityRole();
        this.jwtUtility = jwtUtility;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws JwtException {
        Claims claims;
        String token = (String) authentication.getDetails();
        claims = this.jwtUtility.jwtParse(token);
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
                if (role instanceof String) {
                    grantedAuthorities.add(() -> (String) role);
                }
            }
        }
        return grantedAuthorities;
    }
}
