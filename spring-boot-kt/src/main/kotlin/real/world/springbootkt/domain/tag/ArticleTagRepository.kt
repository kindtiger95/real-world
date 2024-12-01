package real.world.springbootkt.domain.tag

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleTagRepository : JpaRepository<ArticleTag, Long> {
}