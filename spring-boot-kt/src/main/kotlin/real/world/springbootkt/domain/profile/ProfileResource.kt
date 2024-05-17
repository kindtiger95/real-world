package real.world.springbootkt.domain.profile

import real.world.springbootkt.domain.follow.Follow
import real.world.springbootkt.domain.user.User

class ProfileResource {
    data class Response(
        val username: String,
        val following: Boolean,
        val bio: String? = null,
        val image: String? = null
    ) {
        companion object {
            fun from(targetUser: User, following: Boolean): Response {
                return Response(
                    username = targetUser.username,
                    following = following,
                    bio = targetUser.bio,
                    image = targetUser.image
                )
            }
        }
    }
}