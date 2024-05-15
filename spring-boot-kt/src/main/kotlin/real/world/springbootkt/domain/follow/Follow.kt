package real.world.springbootkt.domain.follow

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import real.world.springbootkt.domain.user.User
import real.world.springbootkt.global.common.BaseEntity

@Table(name = "follow")
@Entity
class Follow : BaseEntity() {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    lateinit var follower: User

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", nullable = false)
    lateinit var followee: User
}