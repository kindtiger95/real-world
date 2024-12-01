package real.world.springbootkt.domain.article

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import real.world.springbootkt.domain.profile.ProfileResource
import java.time.OffsetDateTime

class ArticleResources {
    class Response {
        data class ArticlesItem(
            val articles: List<ArticleItem>,
            val articlesCount: Int = articles.size
        )

        data class ArticleItem(
            val slug: String,
            val description: String,
            val body: String,
            val tagList: List<String>,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            val createdAt: OffsetDateTime,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            val updatedAt: OffsetDateTime,
            val favorited: Boolean,
            val favoritesCount: Int,
            val author: ProfileResource.Response.ProfileItem
        ) {
            companion object {
                fun from(
                    article: Article,
                    favorited: Boolean,
                    favoritesCount: Int,
                    profileItem: ProfileResource.Response.ProfileItem
                ): ArticleItem {
                    return ArticleItem(
                        slug = article.slug,
                        description = article.description,
                        body = article.body,
                        tagList = article.articleTags.map { it.tag.tag },
                        createdAt = article.createdAt,
                        updatedAt = article.updatedAt,
                        favorited = favorited,
                        favoritesCount = favoritesCount,
                        author = profileItem
                    )
                }
            }
        }
    }

    class Request {
        @JsonTypeName("article")
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        data class ArticleItem(
            val title: String,
            val description: String,
            val body: String,
            val tagList: List<String> = emptyList()
        )
    }
}