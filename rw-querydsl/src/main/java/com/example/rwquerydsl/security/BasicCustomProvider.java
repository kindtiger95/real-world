package com.example.rwquerydsl.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import com.example.rwquerydsl.common.config.ConfigProvider;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class BasicCustomProvider implements AuthenticationProvider {
    private final String username;
    private final String password;
    private final String role;

    BasicCustomProvider(ConfigProvider configProvider) {
        this.username = configProvider.getBasicUser();
        this.password = configProvider.getBasicPassword();
        this.role = configProvider.getBasicRole();
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
