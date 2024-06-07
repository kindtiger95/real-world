package real.world.springbootkt.domain.profile

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import real.world.springbootkt.domain.follow.Follow
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
        val currentUser = this.userService.getCurrentUser() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "can't find user."
        )
        val targetUser = this.userRepository.findByUsername(username) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "not found target user."
        )
        val following =
            followRepository.findByFollowerIdAndFolloweeId(targetUser.id, currentUser.id)?.let { true } ?: false
        return ProfileResource.Response.from(targetUser, following)
    }

    @Transactional
    fun follow(username: String): ProfileResource.Response {
        val currentUser = this.userService.getCurrentUser() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "can't find user."
        )
        val targetUser = this.userRepository.findByUsername(username) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "not found target user."
        )
        followRepository.findByFollowerIdAndFolloweeId(targetUser.id, currentUser.id)?.let {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "already done request.")
        }
        followRepository.save(Follow().apply {
            this.follower = targetUser
            this.followee = currentUser
        })
        return ProfileResource.Response.from(targetUser, true)
    }

    @Transactional
    fun unfollow(username: String): ProfileResource.Response {
        val currentUser = this.userService.getCurrentUser() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "can't find user."
        )
        val targetUser = this.userRepository.findByUsername(username) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "not found target user."
        )
        val follow = followRepository.findByFollowerIdAndFolloweeId(targetUser.id, currentUser.id) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "already done request."
        )
        followRepository.delete(follow)
        return ProfileResource.Response.from(targetUser, false)
    }
}