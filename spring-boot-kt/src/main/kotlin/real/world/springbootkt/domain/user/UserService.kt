package real.world.springbootkt.domain.user

import io.jsonwebtoken.Claims
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import real.world.springbootkt.global.config.security.JwtAuthenticationToken
import real.world.springbootkt.global.utility.JwtUtility

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtUtility: JwtUtility
) {
    @Transactional
    fun register(request: UserResources.Register.Request): UserResources.User {
        userRepository.findByEmail(request.email)?.let {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "already exist email.")
        }
        val user = userRepository.save(User(request.email, request.username, request.password))
        return UserResources.User.from(user, jwtUtility.jwtSign(user.id, user.username))
    }

    fun login(request: UserResources.Login.Request): UserResources.User {
        val user = userRepository.findByEmail(request.email) ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "is invalid account."
        )
        if (user.password != request.password) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "is invalid account.")
        }
        return UserResources.User.from(user, jwtUtility.jwtSign(user.id, user.username))
    }

    fun me(): UserResources.User {
        val user = this.getCurrentUser() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "can't find user."
        )
        return UserResources.User.from(user, jwtUtility.jwtSign(user.id, user.username))
    }

    @Transactional
    fun update(request: UserResources.Update.Request): UserResources.User {
        val user = this.getCurrentUser()?.apply {
            if (request.username != null) this.username = request.username
            if (request.email != null) this.email = request.email
            if (request.bio != null) this.bio = request.bio
            if (request.image != null) this.image = request.image
            if (request.password != null) this.password = request.password
        } ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "can't find user."
        )
        return UserResources.User.from(user, jwtUtility.jwtSign(user.id, user.username))
    }

    fun getCurrentUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication as? JwtAuthenticationToken
            ?: return null
        if (authentication.isUserInitialized()) {
            return authentication.user
        }
        val claims: Claims = authentication.getPrincipal()
        val userId = (claims["userId"] as? Int)?.toLong() ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "can't find user id info from token."
        )
        val userName = claims["userName"] as? String ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "can't find user name info from token."
        )
        val user: User = userRepository.findByIdOrNull(userId) ?: return null
        if (user.username != userName) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "can't match username.")
        }
        authentication.user = user
        return user
    }
}