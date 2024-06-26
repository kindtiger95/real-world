package real.world.springbootkt.domain.tag

import jakarta.persistence.*
import real.world.springbootkt.global.common.BaseEntity

@Table(name = "tag")
@Entity
class Tag(val tag: String) : BaseEntity() {
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    lateinit var articleTag: List<ArticleTag>
}