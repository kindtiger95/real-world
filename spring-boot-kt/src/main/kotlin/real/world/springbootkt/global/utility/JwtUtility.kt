package real.world.springbootkt.global.utility

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import real.world.springbootkt.global.common.SecurityProperties
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

@Component
class JwtUtility(securityProperties: SecurityProperties) {
    private val signKey: SecretKey = Keys.hmacShaKeyFor(securityProperties.jwtSecretKey.toByteArray(StandardCharsets.UTF_8))
    private val jwtParser: JwtParser = Jwts.parser().verifyWith(this.signKey).build()
    private val userUid = "userUid"
    private val userName = "userName"
    private val jwtRoles: String = securityProperties.jwtRole
    private val securityRole: String = securityProperties.securityRole

    fun jwtParse(jwt: String): Claims = jwtParser.parseSignedClaims(jwt).payload

    fun jwtSign(userUid: Long, userName: String): String {
        return Jwts.builder()
            .claim(this.userUid, userUid)
            .claim(this.userName, userName)
            .claim(this.securityRole, this.jwtRoles)
            .signWith(this.signKey)
            .compact()
    }
}