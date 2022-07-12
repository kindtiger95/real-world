package springboot.configs;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import springboot.security.BasicCustomProvider;
import springboot.security.JwtCustomFilter;
import springboot.security.JwtCustomProvider;

@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomProperties customProperties;

    @Autowired
    public SecurityConfig(
        AuthenticationManagerBuilder authenticationManagerBuilder,
        JwtCustomProvider jwtCustomProvider,
        BasicCustomProvider basicCustomProvider,
        CustomProperties customProperties
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.customProperties = customProperties;
        authenticationManagerBuilder.authenticationProvider(jwtCustomProvider);
        authenticationManagerBuilder.authenticationProvider(basicCustomProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.setSharedObject(AuthenticationManager.class, this.authenticationManagerBuilder.getOrBuild());
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/user/**")
            .hasAnyAuthority(customProperties.getJwtRole())
            .antMatchers("/check/**")
            .hasAnyAuthority(customProperties.getBasicRole())
            .antMatchers("/users/**")
            .permitAll()
            .and()
            .addFilterAfter(new JwtCustomFilter(http.getSharedObject(AuthenticationManager.class)), WebAsyncManagerIntegrationFilter.class)
            //            .addFilterAfter(new JwtCustomFilter(this.authenticationManagerBuilder.getOrBuild()), WebAsyncManagerIntegrationFilter.class)
            .httpBasic()
            .and()
            .requestCache()
            .disable()
            .securityContext()
            .disable()
            .sessionManagement()
            .disable()
            .exceptionHandling()
            .disable()
            .formLogin()
            .disable()
            .logout()
            .disable()
            .headers()
            .disable();
        //            .httpBasic()
        //            .disable()
        //            .anonymous()
        //            .disable();

        return http.build();
    }
}
