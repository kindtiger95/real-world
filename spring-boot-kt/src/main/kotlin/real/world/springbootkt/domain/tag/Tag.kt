package real.world.springbootkt.domain.tag

import jakarta.persistence.*
import real.world.springbootkt.domain.article_tag.ArticleTag
import real.world.springbootkt.domain.common.BaseEntity

@Table(name = "tag")
@Entity
class Tag(
    val tagName: String
) : BaseEntity() {
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val articleTags: MutableList<ArticleTag> = mutableListOf()
}