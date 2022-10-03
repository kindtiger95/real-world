package springboot.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import springboot.domain.dto.TagDto.TagsResDto;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.ArticleTagEntity;
import springboot.domain.entity.TagEntity;
import springboot.repository.ArticleTagRepository;
import springboot.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    @Transactional
    public void createTag(List<String> tagList, ArticleEntity articleEntity) {
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }

        for (String tag : tagList) {
            Optional<TagEntity> tagEntityOpt = this.tagRepository.findByTag(tag);
            if (tagEntityOpt.isEmpty()) {
                tagEntityOpt = Optional.of(TagEntity.builder().tag(tag).build());
                this.tagRepository.save(tagEntityOpt.get());
            }
            ArticleTagEntity articleTagEntity = this.createArticleTagEntity(tagEntityOpt.get(), articleEntity);
            this.articleTagRepository.save(articleTagEntity);
        }
    }

    public TagsResDto getAllTags() {
        List<TagEntity> tagList = this.tagRepository.findAll();
        TagsResDto tagsResDto = new TagsResDto();
        for (TagEntity tagEntity : tagList) {
            tagsResDto.getTags().add(tagEntity.getTag());
        }
        return tagsResDto;
    }

    private ArticleTagEntity createArticleTagEntity(TagEntity tagEntity, ArticleEntity articleEntity) {
        return ArticleTagEntity.builder()
                               .articleEntity(articleEntity)
                               .tagEntity(tagEntity)
                               .build();
    }
}
