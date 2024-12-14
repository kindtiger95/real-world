package real.world.springbootkt.domain.tag

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findByTagNameIn(tags: List<String>): List<Tag>
}