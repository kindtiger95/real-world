package real.world.springbootkt.domain.article_tag

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleTagRepository : JpaRepository<ArticleTag, Long> {
}