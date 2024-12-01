package real.world.springbootkt.domain.tag

import org.springframework.stereotype.Service
import real.world.springbootkt.domain.article.Article

@Service
class TagService(private val tagRepository: TagRepository, private val articleTagRepository: ArticleTagRepository) {
    fun linkTagToArticleTag(tags: List<String>, article: Article): List<ArticleTag> {
        val searchedTags = tagRepository.findByTagIn(tags).toMutableList()
        val tagMap = searchedTags.associateBy { it.tag }
        tags.forEach {
            if (tagMap[it] == null) {
                searchedTags.add(tagRepository.save(Tag().apply { this.tag = it }))
            }
        }
        val articleTags = searchedTags.map {
            ArticleTag().apply {
                this.article = article
                this.tag = it
            }
        }
        return articleTagRepository.saveAll(articleTags)
    }
}