package real.world.springbootkt.domain.article

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ArticleQuerydslRepository(private val jpaQueryFactory: JPAQueryFactory) {

}