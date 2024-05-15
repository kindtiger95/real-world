package real.world.springbootkt.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
import real.world.springbootkt.global.common.SecurityProperties
import real.world.springbootkt.global.filter.JwtFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val securityProperties: SecurityProperties,
    private val objectMapper: ObjectMapper,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    jwtProvider: JwtProvider
) {
    init {
        authenticationManagerBuilder.authenticationProvider(jwtProvider)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.setSharedObject(
            AuthenticationManager::class.java,
            this.authenticationManagerBuilder.getOrBuild()
        )
        return http.csrf { it.disable() }
            .cors { it.disable() }
            .httpBasic { it.disable() }
            .requestCache { it.disable() }
            .securityContext { it.disable() }
            .sessionManagement { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .headers { it.disable() }
            .addFilterAfter(
                JwtFilter(http.getSharedObject(AuthenticationManager::class.java)),
                WebAsyncManagerIntegrationFilter::class.java
            )
            .authorizeHttpRequests {
                it.requestMatchers("/user/**")
                    .hasAuthority(securityProperties.jwtRole)
                    .requestMatchers("/profiles/{username}/**")
                    .hasAuthority(securityProperties.jwtRole)
                    .anyRequest()
                    .permitAll()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper))
                    .accessDeniedHandler(CustomAccessDeniedHandler(objectMapper))
            }
            .build()
    }
}