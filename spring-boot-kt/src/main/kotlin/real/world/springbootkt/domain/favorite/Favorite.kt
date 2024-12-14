package real.world.springbootkt.domain.favorite

import jakarta.persistence.*
import real.world.springbootkt.domain.article.Article
import real.world.springbootkt.domain.user.User
import real.world.springbootkt.domain.common.BaseEntity

@Entity
@Table(name = "favorite")
class Favorite : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    lateinit var user: User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    lateinit var article: Article
}