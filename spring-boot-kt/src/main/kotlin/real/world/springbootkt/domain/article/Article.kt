package real.world.springbootkt.domain.article

import jakarta.persistence.*
import real.world.springbootkt.domain.comment.Comment
import real.world.springbootkt.domain.favorite.Favorite
import real.world.springbootkt.domain.article_tag.ArticleTag
import real.world.springbootkt.domain.user.User
import real.world.springbootkt.domain.common.BaseEntity

@Table(name = "article")
@Entity
class Article : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    lateinit var user: User

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val articleTags: MutableList<ArticleTag> = mutableListOf()

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    val favorites: MutableList<Favorite> = mutableListOf()

    @Column(nullable = false)
    var title: String = ""
        set(value) {
            field = value
            this.slug = value.lowercase().replace(" ", "-")
        }

    @Column(nullable = false)
    var slug: String = ""
        protected set

    lateinit var description: String

    lateinit var body: String

    @Column(nullable = false)
    lateinit var username: String
}