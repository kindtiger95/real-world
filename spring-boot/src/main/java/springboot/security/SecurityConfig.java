package springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
public class SecurityConfig {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public SecurityConfig(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            JwtCustomProvider jwtCustomProvider
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authenticationManagerBuilder.authenticationProvider(jwtCustomProvider);
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
        http.logout().disable();
        http.headers().disable();
        http.addFilterBefore(new JwtCustomFilter(this.authenticationManagerBuilder.getOrBuild()), WebAsyncManagerIntegrationFilter.class);
        return http.build();
    }
}
