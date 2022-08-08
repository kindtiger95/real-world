package springboot.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import springboot.domains.response.ResponseAuthorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtCustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        ErrorCode unauthorized = ErrorCode.UNAUTHORIZED;
        if (exception.equals(unauthorized.getCode())) {
            this.setResponse(response, unauthorized);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpCode());
        ResponseAuthorization responseAuthorization = ResponseAuthorization.builder()
                                                                           .code(errorCode.getCode())
                                                                           .message(errorCode.getMessage())
                                                                           .build();
        String json = this.objectMapper.writeValueAsString(responseAuthorization);
        response.getWriter()
                .print(json);
    }
}
