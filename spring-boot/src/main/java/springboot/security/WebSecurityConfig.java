package springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestCache().disable();
        http.anonymous().disable();
        http.securityContext().disable();
        http.sessionManagement().disable();
        http.exceptionHandling().disable();
        http.httpBasic().disable();
        http.logout().disable();
        http.headers().disable();
        return http.build();
    }
}
