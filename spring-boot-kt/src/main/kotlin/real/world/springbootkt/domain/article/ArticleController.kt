package real.world.springbootkt.domain.article

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ArticleController(private val articleService: ArticleService) {
    @GetMapping("/articles")
    fun getArticles(
        @RequestParam(name = "tag") tag: String? = null,
        @RequestParam(name = "author") author: String? = null,
        @RequestParam(name = "favorited") favorited: String? = null,
        @RequestParam(name = "limit", defaultValue = "20") limit: Long,
        @RequestParam(name = "offset", defaultValue = "0") offset: Long
    ): ArticleResources.Response.ArticlesItem =
        articleService.getArticles(tag, author, favorited, limit, offset)

    @PostMapping("/articles")
    fun createArticle(@RequestBody request: ArticleResources.Request.ArticleItem): ArticleResources.Response.ArticleItem =
        articleService.createArticle(request)
}