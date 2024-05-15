package real.world.springbootkt.domain.profile

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import real.world.springbootkt.domain.follow.FollowRepository
import real.world.springbootkt.domain.user.UserRepository
import real.world.springbootkt.domain.user.UserService

@Service
class ProfileService(
    private val userService: UserService,
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getProfileByUsername(username: String): ProfileResource.Response {
        val currentUser = this.userService.getCurrentUser()
        val targetUser = this.userRepository.findByUsername(username)
        followRepository.

    }
}