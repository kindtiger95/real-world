package real.world.springbootkt.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import real.world.springbootkt.global.common.ErrorCode

class CustomAuthenticationEntryPoint(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        val exception = request.getAttribute("exception") as? String
        val unauthorized: ErrorCode = ErrorCode.UNAUTHORIZED
        if (exception == unauthorized.code) {
            this.setResponse(response, unauthorized)
        }
    }

    private fun setResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        response.apply {
            this.contentType = "application/json;charset=UTF-8"
            this.status = errorCode.httpCode
        }
        val json = objectMapper.writeValueAsString(errorCode)
        response.writer.print(json)
    }
}