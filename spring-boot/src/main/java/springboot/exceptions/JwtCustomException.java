package springboot.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtCustomException extends AuthenticationException {
    public JwtCustomException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtCustomException(String msg) {
        super(msg);
    }
}
