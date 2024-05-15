package real.world.springbootkt.domain.profile

class ProfileResource {
    data class Response(
        val username: String,
        val following: Boolean,
        val bio: String? = null,
        val image: String? = null
    )
}