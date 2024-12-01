package real.world.springbootkt.domain.article

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import real.world.springbootkt.domain.article.QArticle.article
import real.world.springbootkt.domain.favorite.QFavorite.favorite
import real.world.springbootkt.domain.tag.QArticleTag.articleTag
import real.world.springbootkt.domain.tag.QTag.tag1
import real.world.springbootkt.domain.user.QUser.user

class ArticleQuerydslRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory): ArticleQuerydslRepository {
    override fun findAllByTagAndAuthorAndFavorited(
        tag: String?,
        author: String?,
        favorited: String?,
        limit: Long,
        offset: Long
    ): List<Article> {
        var selectFrom = jpaQueryFactory.selectFrom(article)
        if (tag != null) {
            selectFrom = selectFrom.join(article.articleTags, articleTag)
                .join(articleTag.tag, tag1)
                .where(tag1.tag.eq(tag))
        } else if (author != null) {
            selectFrom = selectFrom.where(article.username.eq(author))
        } else if (favorited != null) {
            selectFrom = selectFrom.join(article.favorites, favorite)
                .join(favorite.user, user)
                .where(user.username.eq(favorited))
        }
        return selectFrom.orderBy(article.createdAt.desc())
            .offset(offset)
            .limit(limit)
            .fetch()
    }
}