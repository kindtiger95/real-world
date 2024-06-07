package real.world.springbootkt.domain.article

import org.springframework.stereotype.Service
import real.world.springbootkt.domain.user.UserService

@Service
class ArticleService(private val articleRepository: ArticleRepository, private val userService: UserService) {
    fun getArticles(tag: String?, author: String?, favorited: Boolean, limit: Long, offset: Long): List<ArticleResources.Article> {
        val currentUser = userService.getCurrentUser()
        articleRepository.findAllByTagAndAuthorAndFavorited(tag, author, favorited, limit, offset, currentUser?.id)
        return emptyList()
    }
}