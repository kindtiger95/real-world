package springboot.security;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import springboot.CustomProperties;

@Component
public class JwtCustomProvider implements AuthenticationProvider {

    private final byte[] secretKeyByte;

    public JwtCustomProvider(CustomProperties customProperties) {
        this.secretKeyByte = customProperties.getJwtSecretKey().getBytes();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtCustomToken.class.isAssignableFrom(authentication);
    }
}
