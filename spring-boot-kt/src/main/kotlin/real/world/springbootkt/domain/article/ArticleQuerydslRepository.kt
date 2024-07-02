package real.world.springbootkt.domain.article

interface ArticleQuerydslRepository {
    fun findAllByTagAndAuthorAndFavorited(
        tag: String?,
        author: String?,
        favorited: String?,
        limit: Long,
        offset: Long
    ): List<Article>
}