package springboot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import springboot.common.enums.ErrorCode;
import springboot.domain.dto.AuthDto;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        ErrorCode unauthorized = ErrorCode.UNAUTHORIZED;
        if (exception.equals(unauthorized.getCode())) {
            this.setResponse(response, unauthorized);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpCode());
        AuthDto responseAuthorization = AuthDto.builder()
                                               .body(new ArrayList<>(List.of("Authentication failed.")))
                                               .build();
        String json = this.objectMapper.writeValueAsString(responseAuthorization);
        response.getWriter()
                .print(json);
    }
}
