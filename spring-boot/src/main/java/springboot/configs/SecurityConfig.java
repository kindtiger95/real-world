package springboot.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import springboot.security.JwtCustomFilter;
import springboot.security.JwtCustomProvider;

@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public SecurityConfig(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            JwtCustomProvider jwtCustomProvider
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        authenticationManagerBuilder.authenticationProvider(jwtCustomProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(new JwtCustomFilter(this.authenticationManagerBuilder.getOrBuild()), WebAsyncManagerIntegrationFilter.class)
                .requestCache().disable()
                .anonymous().disable()
                .securityContext().disable()
                .sessionManagement().disable()
                .exceptionHandling().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .headers().disable();
        return http.build();
    }
}
