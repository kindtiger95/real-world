package real.world.springbootkt.domain.article

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import real.world.springbootkt.domain.favorite.FavoriteRepository
import real.world.springbootkt.domain.profile.ProfileResource
import real.world.springbootkt.domain.profile.ProfileService
import real.world.springbootkt.domain.tag.TagService
import real.world.springbootkt.domain.user.UserService
import kotlin.jvm.optionals.getOrNull

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userService: UserService,
    private val profileService: ProfileService,
    private val favoriteRepository: FavoriteRepository,
    private val tagService: TagService
) {
    @Transactional(readOnly = true)
    fun getArticles(
        tag: String?,
        author: String?,
        favorited: String?,
        limit: Long,
        offset: Long
    ): ArticleResources.Response.ArticlesItem {
        val articles = articleRepository.findAllByTagAndAuthorAndFavorited(tag, author, favorited, limit, offset)
        val profileResponseMapByArticleId = profileService.getProfileMapByArticleId(articles)
        val user = userService.getCurrentUser()
        return ArticleResources.Response.ArticlesItem(
            articles.map {
                ArticleResources.Response.ArticleItem.from(
                    it,
                    it.favorites.firstOrNull { favorite -> favorite.user.id == user?.id } != null,
                    it.favorites.size,
                    profileResponseMapByArticleId[it.id]!!)
            }
        )
    }

    @Transactional
    fun createArticle(request: ArticleResources.Request.ArticleItem): ArticleResources.Response.ArticleItem {
        val currentUser = this.userService.getCurrentUser() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "can't find user."
        )
        val article = Article().apply {
            this.title = request.title
            this.user = currentUser
            this.username = currentUser.username
            this.body = request.body
            this.description = request.description
            this.setSlug()
        }
        articleRepository.save(article)
        if (request.tagList.isNotEmpty()) {
            article.articleTags.addAll(tagService.linkTagToArticleTag(request.tagList, article))
        }
        return ArticleResources.Response.ArticleItem.from(
            article,
            false,
            0,
            ProfileResource.Response.ProfileItem(
                currentUser.username,
                false,
                currentUser.bio,
                currentUser.image
            )
        )
    }
}