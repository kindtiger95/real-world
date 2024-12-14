package real.world.springbootkt.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import real.world.springbootkt.domain.common.BaseEntity

@Table(name = "user")
@Entity
class User(
    @Column(nullable = false)
    var email: String,
    @Column(nullable = false)
    var username: String,
    @Column(nullable = false)
    var password: String,
) : BaseEntity() {
    var bio: String? = null
    var image: String? = null

    fun updateInfo(username: String?, email: String?, bio: String?, image: String?, password: String?) {
        if (username != null) this.username = username
        if (email != null) this.email = email
        if (bio != null) this.bio = bio
        if (image != null) this.image = image
        if (password != null) this.password = password
    }
}