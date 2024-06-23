package real.world.springbootkt.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import real.world.springbootkt.global.common.ErrorCode
import real.world.springbootkt.global.config.security.JwtAuthenticationToken

private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER_PREFIX = "Token "

class JwtFilter(private val authenticationManager: AuthenticationManager) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        getToken(request)?.let {
            try {
                val authentication = authenticationManager.authenticate(JwtAuthenticationToken(it))
                SecurityContextHolder.getContext().authentication = authentication
            } catch (authenticationException: AuthenticationException) {
                SecurityContextHolder.clearContext()
                request.setAttribute("exception", ErrorCode.INTERNAL_SERVER_ERROR.code)
            }
        }
        filterChain.doFilter(request, response)
    }

    /*********************************** Private Function ***********************************/
    private fun getToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader(AUTHORIZATION_HEADER)
        return when {
            authHeader == null -> {
                request.setAttribute("exception", ErrorCode.UNAUTHORIZED.code)
                null
            }
            authHeader.startsWith(BEARER_PREFIX) -> authHeader.substring(BEARER_PREFIX.length)
            else -> {
                request.setAttribute("exception", ErrorCode.UNAUTHORIZED.code)
                null
            }
        }
    }
}