package springboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import springboot.configs.CustomProperties;

@Component
public class JwtUtility {

    private final JwtParser jwtParser;
    private final SecretKey signKey;

    private final String userUid = "userUid";
    private final String userName = "userName";
    private final List<String> jwtRoles;
    private final String securityRole;

    public JwtUtility(CustomProperties customProperties) {
        String rawSecretKey = customProperties.getJwtSecretKey();
        this.jwtRoles = new ArrayList<>(Collections.singletonList(customProperties.getJwtRole()));
        this.securityRole = customProperties.getSecurityRole();
        this.signKey = Keys.hmacShaKeyFor(rawSecretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder()
                             .setSigningKey(this.signKey)
                             .build();
    }

    public Claims jwtParse(String jwt) throws JwtException {
        return this.jwtParser.parseClaimsJws(jwt)
                             .getBody();
    }

    public String jwtSign(Long userUid, String userName) {
        return Jwts.builder()
                   .claim(this.userUid, String.valueOf(userUid))
                   .claim(this.userName, userName)
                   .claim(this.securityRole, this.jwtRoles)
                   .signWith(this.signKey)
                   .compact();
    }

    public Long getUserUid(Claims claims) {
        return Long.parseLong((String) claims.get(this.userUid));
    }

    public String getUserName(Claims claims) {
        return (String) claims.get(this.userName);
    }
}
