package real.world.springbootkt.domain.article

interface ArticleQuerydslRepository {
    fun findAllByTagAndAuthorAndFavorited(
        tag: String?,
        author: String?,
        favorited: Boolean,
        limit: Long,
        offset: Long,
        userId: Long?
    ): List<Article>
}