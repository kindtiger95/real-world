package real.world.springbootkt.domain.tag

import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByTagIn(tags: List<String>): List<Tag>
}