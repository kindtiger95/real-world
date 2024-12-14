package real.world.springbootkt.domain.comment

import jakarta.persistence.*
import real.world.springbootkt.domain.article.Article
import real.world.springbootkt.domain.user.User
import real.world.springbootkt.domain.common.BaseEntity

@Table(name = "comment")
@Entity
class Comment(
    val body: String
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    lateinit var article: Article

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    lateinit var user: User
}