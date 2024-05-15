package real.world.springbootkt.domain.tag

import jakarta.persistence.*
import real.world.springbootkt.domain.article.Article
import real.world.springbootkt.global.common.BaseEntity

@Table(name = "article_tag")
@Entity
class ArticleTag : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    lateinit var tag: Tag

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    lateinit var article: Article
}