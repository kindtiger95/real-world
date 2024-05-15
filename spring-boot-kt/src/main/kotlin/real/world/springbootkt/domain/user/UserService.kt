package real.world.springbootkt.domain.user

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun login(request: UserResources.Login.Request): UserResources.User {
        val user = userRepository.findByEmail(request.email) ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "is invalid account."
        )
        if (user.password != request.password) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "is invalid account.")
        }
        return UserResources.User(user.email, "1234", user.username, user.bio, user.image)
    }

    @Transactional
    fun register(request: UserResources.Register.Request): UserResources.User {
        userRepository.findByEmail(request.email)?.let {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "already exist email.")
        }
        val user = userRepository.save(User(request.email, request.username, request.password))
        return UserResources.User(user.email, "1234", user.username)
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
        return UserResources.User(
            user.email,
            "1234",
            user.username,
            user.bio,
            user.image
        )
    }
}