package real.world.springbootkt.domain.common

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(updatable = false, nullable = false)
    @CreatedDate
    var createdAt: OffsetDateTime = OffsetDateTime.now()
        protected set

    @Column(nullable = false)
    @LastModifiedDate
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
        protected set
}