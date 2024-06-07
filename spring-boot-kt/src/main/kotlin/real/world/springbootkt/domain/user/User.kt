package real.world.springbootkt.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import real.world.springbootkt.global.common.BaseEntity

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
}