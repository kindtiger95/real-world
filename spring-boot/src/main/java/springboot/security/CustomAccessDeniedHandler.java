package springboot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import springboot.common.enums.ErrorCode;
import springboot.domain.dto.response.AuthResDto;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode unauthorized = ErrorCode.UNAUTHORIZED;
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(unauthorized.getHttpCode());

        AuthResDto authResDto = AuthResDto.builder()
                                          .body(new ArrayList<>(List.of("Access Denied.")))
                                          .build();
        String json = this.objectMapper.writeValueAsString(authResDto);
        response.getWriter()
                .print(json);
    }
}
