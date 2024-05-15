package real.world.springbootkt.domain.user

class UserResources {
    class Login {
        data class Request(
            val email: String,
            val password: String
        )
    }

    class Register {
        data class Request(
            val email: String,
            val username: String,
            val password: String
        )
    }

    class Update {
        data class Request(
            val email: String,
            val bio: String,
            val image: String,
            val username: String,
            val password: String
        )
    }

    data class User(
        val email: String,
        val token: String,
        val username: String,
        val bio: String? = null,
        val image: String? = null,
    )
}