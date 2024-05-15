package real.world.springbootkt.domain.tag

import jakarta.persistence.Entity
import jakarta.persistence.Table
import real.world.springbootkt.global.common.BaseEntity

@Table(name = "tag")
@Entity
class Tag : BaseEntity() {
    var tag: String? = null
}