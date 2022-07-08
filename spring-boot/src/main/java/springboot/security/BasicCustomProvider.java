package springboot.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import springboot.configs.CustomProperties;

import java.util.ArrayList;
import java.util.List;

@Component
public class BasicCustomProvider implements AuthenticationProvider {
    private final String username;
    private final String password;
    private final String role;

    BasicCustomProvider(CustomProperties customProperties) {
        this.username = customProperties.getBasicUser();
        this.password = customProperties.getBasicPassword();
        this.role = customProperties.getBasicRole();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String reqUsername = (String) authentication.getPrincipal();
        String reqPassword = (String) authentication.getCredentials();
        if (reqUsername.equals(this.username) && reqPassword.equals(this.password)) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(() -> (String) this.role);
            return new UsernamePasswordAuthenticationToken(reqUsername, reqPassword, grantedAuthorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
