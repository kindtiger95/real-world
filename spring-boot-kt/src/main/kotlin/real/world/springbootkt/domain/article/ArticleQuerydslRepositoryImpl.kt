package real.world.springbootkt.domain.article

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import real.world.springbootkt.domain.article.QArticle.article
import real.world.springbootkt.domain.like.QLike.like
import real.world.springbootkt.domain.tag.QArticleTag.articleTag
import real.world.springbootkt.domain.tag.QTag.tag1

@Repository
class ArticleQuerydslRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory): ArticleQuerydslRepository {
    override fun findAllByTagAndAuthorAndFavorited(
        tag: String?,
        author: String?,
        favorited: Boolean,
        limit: Long,
        offset: Long,
        userId: Long?
    ): List<Article> {
        var selectFrom = jpaQueryFactory.selectFrom(article)
        if (tag != null) {
            selectFrom = selectFrom.join(article.articleTag, articleTag)
                .join(articleTag.tag, tag1)
                .where(tag1.tag.eq(tag))
        }
        if (author != null) {
            selectFrom = selectFrom.where(article.username.eq(author))
        }
        if (favorited && userId != null) {
            selectFrom = selectFrom.join(article.like, like)
                .where(like.user.id.eq(userId))
        }
        return selectFrom.offset(offset)
            .limit(limit)
            .fetch()
    }
}