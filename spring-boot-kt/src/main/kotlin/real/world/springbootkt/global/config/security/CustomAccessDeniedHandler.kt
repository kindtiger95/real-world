package real.world.springbootkt.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import real.world.springbootkt.global.common.ErrorCode

class CustomAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest, response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val forbidden: ErrorCode = ErrorCode.FORBIDDEN
        response.apply {
            this.contentType = "application/json;charset=UTF-8"
            this.status = forbidden.httpCode
        }
        val json = objectMapper.writeValueAsString(forbidden)
        response.writer.print(json)
    }
}