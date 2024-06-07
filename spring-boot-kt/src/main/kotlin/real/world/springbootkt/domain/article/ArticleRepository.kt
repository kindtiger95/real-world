package real.world.springbootkt.domain.article

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface ArticleRepository : JpaRepository<Article, Long>, ArticleQuerydslRepository