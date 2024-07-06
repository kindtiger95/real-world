package real.world.springbootkt.domain.favorite

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import real.world.springbootkt.domain.article.Article
import real.world.springbootkt.domain.user.User

@Repository
interface FavoriteRepository : JpaRepository<Favorite, Long> {
    fun findByUserAndArticleIn(user: User, articles: List<Article>): List<Favorite>
}