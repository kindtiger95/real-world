package real.world.springbootkt.domain.user

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

class UserResources {
    class Login {
        @JsonTypeName("user")
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        data class Request(
            val email: String,
            val password: String
        )
    }

    class Register {
        @JsonTypeName("user")
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        data class Request(
            val email: String,
            val username: String,
            val password: String
        )
    }

    class Update {
        @JsonTypeName("user")
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        data class Request(
            val email: String?,
            val bio: String?,
            val image: String?,
            val username: String?,
            val password: String?
        )
    }

    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    data class User(
        val email: String,
        val token: String,
        val username: String,
        val bio: String? = null,
        val image: String? = null,
    ) {
        companion object {
            fun from(user: real.world.springbootkt.domain.user.User, token: String): User {
                return User(
                    email = user.email,
                    token = token,
                    username = user.username,
                    bio = user.bio,
                    image = user.image
                )
            }
        }
    }
}