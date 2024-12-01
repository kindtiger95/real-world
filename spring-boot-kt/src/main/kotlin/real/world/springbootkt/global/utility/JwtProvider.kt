package real.world.springbootkt.global.utility

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import real.world.springbootkt.global.common.SecurityProperties
import real.world.springbootkt.global.config.security.JwtAuthenticationToken

@Component
class JwtProvider(
    securityProperties: SecurityProperties,
    private val jwtUtility: JwtUtility
) : AuthenticationProvider {
    private val securityRole: String = securityProperties.securityRole

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.details as String
        val claims: Claims = jwtUtility.jwtParse(token)
        return JwtAuthenticationToken(claims, token, this.createGrantedAuthorities(claims))
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun createGrantedAuthorities(claims: Claims): Collection<GrantedAuthority> {
        val grantedAuthorities: MutableList<GrantedAuthority> = ArrayList()
        val role = claims[securityRole]
        if (role is String) {
            grantedAuthorities.add(GrantedAuthority { role })
        }
        return grantedAuthorities
    }
}
