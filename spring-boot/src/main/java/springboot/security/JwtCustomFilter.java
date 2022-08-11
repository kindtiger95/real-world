package springboot.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot.exceptions.ErrorCode;

@RequiredArgsConstructor
public class JwtCustomFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_PREFIX = "Token ";
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getToken(request);
        if (jwtToken != null) {
            try {
                Authentication authentication = authenticationManager.authenticate(new JwtCustomToken(jwtToken));
                SecurityContextHolder.getContext()
                                     .setAuthentication(authentication);
            } catch (AuthenticationException authenticationException) {
                SecurityContextHolder.clearContext();
                request.setAttribute("exception", ErrorCode.UNAUTHORIZED.getCode());
            }
        }
        filterChain.doFilter(request, response);
    }

    /*********************************** Private Function **********************************/
    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authHeader == null ? null
            : authHeader.startsWith(BEARER_PREFIX) ? authHeader.substring(BEARER_PREFIX.length()) : null;
    }
}
