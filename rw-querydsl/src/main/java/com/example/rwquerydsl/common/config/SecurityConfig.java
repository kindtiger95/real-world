package com.example.rwquerydsl.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import com.example.rwquerydsl.security.CustomAccessDeniedHandler;
import com.example.rwquerydsl.security.CustomAuthenticationEntryPoint;
import com.example.rwquerydsl.security.JwtCustomFilter;
import com.example.rwquerydsl.security.JwtCustomProvider;

@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ConfigProvider configProvider;

    @Autowired
    public SecurityConfig(
        AuthenticationManagerBuilder authenticationManagerBuilder,
        JwtCustomProvider jwtCustomProvider,
        ConfigProvider configProvider
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.configProvider = configProvider;
        authenticationManagerBuilder.authenticationProvider(jwtCustomProvider);
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
            .hasAuthority(configProvider.getJwtRole())
            .antMatchers("/profiles/{username}/**")
            .hasAuthority(configProvider.getJwtRole())
//            .antMatchers("/check/**")
//            .hasAuthority(configProvider.getBasicRole())
            .anyRequest()
            .permitAll()
            .and()
            .addFilterAfter(new JwtCustomFilter(http.getSharedObject(AuthenticationManager.class)), WebAsyncManagerIntegrationFilter.class)
            //            .addFilterAfter(new JwtCustomFilter(this.authenticationManagerBuilder.getOrBuild()), WebAsyncManagerIntegrationFilter.class)
            .httpBasic().disable()
            .requestCache().disable()
            .securityContext().disable()
            .sessionManagement().disable()
            .formLogin().disable()
            .logout().disable()
            .headers().disable()
            .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                .accessDeniedHandler(new CustomAccessDeniedHandler());
        //  .exceptionHandling().disable();
        //  .anonymous().disable();
        return http.build();
    }
}
