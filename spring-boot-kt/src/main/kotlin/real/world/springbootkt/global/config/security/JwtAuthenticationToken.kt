package real.world.springbootkt.global.config.security

import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import real.world.springbootkt.domain.user.User

class JwtAuthenticationToken : AbstractAuthenticationToken {
    private lateinit var credentials: String
    private lateinit var principal: Claims
    lateinit var user: User

    constructor(jsonWebToken: String) : super(null) {
        this.details = jsonWebToken
        this.isAuthenticated = false
    }

    constructor(
        principal: Claims,
        credentials: String,
        authorities: Collection<GrantedAuthority>
    ) : super(authorities) {
        this.principal = principal
        this.credentials = credentials
        super.setAuthenticated(true)
    }

    override fun getCredentials() = this.credentials

    override fun getPrincipal() = this.principal

    fun isUserInitialized() = this::user.isInitialized
}