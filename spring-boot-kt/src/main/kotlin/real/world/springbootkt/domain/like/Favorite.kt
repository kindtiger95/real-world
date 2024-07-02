package real.world.springbootkt.domain.like

import jakarta.persistence.*
import real.world.springbootkt.domain.article.Article
import real.world.springbootkt.domain.user.User
import real.world.springbootkt.global.common.BaseEntity

@Table(name = "favorite")
@Entity
class Favorite : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    lateinit var article: Article
}