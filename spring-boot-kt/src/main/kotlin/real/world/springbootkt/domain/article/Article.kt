package real.world.springbootkt.domain.article

import jakarta.persistence.*
import real.world.springbootkt.domain.comment.Comment
import real.world.springbootkt.domain.user.User
import real.world.springbootkt.global.common.BaseEntity

@Table(name = "article")
@Entity
class Article(
    title: String
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    lateinit var comments: List<Comment>

    @Column(nullable = false)
    var title: String = title
        set(value) {
            this.slug = title.lowercase().replace(" ", "-")
            field = value
        }

    @Column(nullable = false)
    lateinit var slug: String

    var description: String? = null

    var body: String? = null
}