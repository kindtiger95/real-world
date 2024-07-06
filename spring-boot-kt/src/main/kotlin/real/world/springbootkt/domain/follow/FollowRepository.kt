package real.world.springbootkt.domain.follow

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository : JpaRepository<Follow, Long> {
    fun findByFollowerIdAndFolloweeId(followerId: Long, followeeId: Long): Follow?
    fun findByFollowerIdInAndFolloweeId(followerIdIn: List<Long>, followeeId: Long): List<Follow>
}