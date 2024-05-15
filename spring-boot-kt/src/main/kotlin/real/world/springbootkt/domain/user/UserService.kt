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
    private val logger = LoggerFactory.getLogger(this::class.java.name)

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
        val user = this.getCurrentUser()
        return UserResources.User.from(user, jwtUtility.jwtSign(user.id, user.username))
    }

    @Transactional
    fun update(request: UserResources.Update.Request): UserResources.User {
        val user = userRepository.findByEmail(request.email)?.apply {
            this.bio = request.bio
            this.image = request.image
            this.password = request.password
        } ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "not found email."
        )
        return UserResources.User.from(user, jwtUtility.jwtSign(user.id, user.username))
    }

    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication as? JwtAuthenticationToken
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "no auth info.")
        val claims: Claims = authentication.getPrincipal()
        val userId = (claims["userId"] as? Int)?.toLong() ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "can't find user id info from token."
        )
        val userName = claims["userName"] as? String ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "can't find user name info from token."
        )
        val user: User = userRepository.findByIdOrNull(userId) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "can't find user."
        )
        if (user.username != userName) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "can't match username.")
        }
        return user
    }
}