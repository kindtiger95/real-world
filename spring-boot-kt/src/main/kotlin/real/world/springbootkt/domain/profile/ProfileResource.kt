package real.world.springbootkt.domain.profile

import real.world.springbootkt.domain.user.User

class ProfileResource {
    data class Response(
        val profile: ProfileItem,
    ) {
        data class ProfileItem(
            val username: String,
            val following: Boolean,
            val bio: String? = null,
            val image: String? = null
        ) {
            companion object {
                fun from(targetUser: User, following: Boolean): ProfileItem {
                    return ProfileItem(
                        username = targetUser.username,
                        following = following,
                        bio = targetUser.bio,
                        image = targetUser.image
                    )
                }
            }
        }
    }
}