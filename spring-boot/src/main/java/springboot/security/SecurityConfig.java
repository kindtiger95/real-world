package springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import java.util.ArrayList;

@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final ProviderManager providerManager;

    public SecurityConfig(
            JwtCustomProvider jwtCustomProvider
    ) {
        ArrayList<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(jwtCustomProvider);
        this.providerManager = new ProviderManager(authenticationProviders);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestCache().disable();
        http.anonymous().disable();
        http.securityContext().disable();
        http.sessionManagement().disable();
        http.exceptionHandling().disable();
        http.httpBasic().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.headers().disable();
        http.addFilterBefore(new JwtCustomFilter(this.providerManager), WebAsyncManagerIntegrationFilter.class);
        return http.build();
    }
}
