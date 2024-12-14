package real.world.springbootkt.domain.article_tag

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import real.world.springbootkt.domain.article.Article
import real.world.springbootkt.domain.tag.Tag
import real.world.springbootkt.domain.tag.TagRepository
import real.world.springbootkt.domain.user.User

@Service
class ArticleTagService(
    private val tagRepository: TagRepository,
    private val articleTagRepository: ArticleTagRepository
) {
    @Transactional(propagation = Propagation.MANDATORY)
    fun linkTagToArticleTag(tags: List<String>, article: Article, currentUser: User): List<ArticleTag> {
        val savedTags = tagRepository.findByTagNameIn(tags).toMutableList()
        val savedTagMap = savedTags.associateBy { it.tagName }
        tags.forEach { savedTagMap[it] ?: savedTags.add(tagRepository.save(Tag(tagName = it))) }
        val articleTags = savedTags.map {
            ArticleTag().apply {
                this.article = article
                this.tag = it
            }
        }
        return articleTagRepository.saveAll(articleTags)
    }
}