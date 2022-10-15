package com.example.rwquerydsl.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.example.rwquerydsl.common.exception.JwtCustomException;
import com.example.rwquerydsl.common.utility.JwtUtility;
import com.example.rwquerydsl.common.config.ConfigProvider;

@Component
public class JwtCustomProvider implements AuthenticationProvider {

    private final String securityRole;
    private final JwtUtility jwtUtility;

    public JwtCustomProvider(ConfigProvider configProvider, JwtUtility jwtUtility) {
        this.securityRole = configProvider.getSecurityRole();
        this.jwtUtility = jwtUtility;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getDetails();
        try {
            Claims claims = this.jwtUtility.jwtParse(token);
            return new JwtCustomToken(claims, token, this.createGrantedAuthorities(claims));
        } catch (JwtException jwtException) {
            throw new JwtCustomException("토큰 유효하지 않음");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtCustomToken.class.isAssignableFrom(authentication);
    }

    private Collection<? extends GrantedAuthority> createGrantedAuthorities(Claims claims) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Object role = claims.get(this.securityRole);
        if (role instanceof String) {
            grantedAuthorities.add(() -> (String) role);
        }

        return grantedAuthorities;
    }
}
