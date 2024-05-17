package real.world.springbootkt.domain.article

import real.world.springbootkt.domain.profile.ProfileResource
import java.time.OffsetDateTime

class ArticleResources {
    data class Article(
        val slug: String,
        val description: String,
        val body: String,
        val tagList: List<String>,
        val createdAt: OffsetDateTime,
        val updatedAt: OffsetDateTime,
        val favorited: Boolean,
        val favoritesCount: Int,
        val author: ProfileResource.Response
    )
}