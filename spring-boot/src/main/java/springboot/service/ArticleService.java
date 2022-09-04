package springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.dto.ArticleDto.CreateArticleReqDto;
import springboot.domain.dto.ArticleDto.MultipleArticleResDto;
import springboot.domain.dto.ArticleDto.SingleArticleResDto;
import springboot.domain.dto.ArticleDto.UpdateArticleReqDto;
import springboot.domain.dto.ProfileDto;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.UserEntity;
import springboot.repository.ArticleRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final LookupService lookupService;
    private final TagService tagService;

    @Transactional
    public SingleArticleResDto createArticle(CreateArticleReqDto createArticleReqDto) {
        UserEntity userEntity = this.lookupService.getCurrentUserEntity()
                                                  .orElseThrow(() -> new RuntimeException("로그인 유저가 아닙니다."));

        String slug = this.makeSlug(createArticleReqDto.getTitle());
        ArticleEntity articleEntity = ArticleEntity.builder()
                                                   .title(createArticleReqDto.getTitle())
                                                   .body(createArticleReqDto.getBody())
                                                   .slug(slug)
                                                   .description(createArticleReqDto.getDescription())
                                                   .userEntity(userEntity)
                                                   .build();
        this.articleRepository.save(articleEntity);
        this.tagService.createTag(createArticleReqDto.getTagList(), articleEntity);

        ProfileDto profileDto = createProfileDto(userEntity);
        return SingleArticleResDto.builder()
                                  .slug(slug)
                                  .title(articleEntity.getTitle())
                                  .description(articleEntity.getDescription())
                                  .body(articleEntity.getBody())
                                  .tagList(createArticleReqDto.getTagList())
                                  .createdAt(articleEntity.getCreatedAt()
                                                          .toString())
                                  .updatedAt(articleEntity.getUpdatedAt()
                                                          .toString())
                                  .favorite(false)
                                  .favoritesCount(0)
                                  .author(profileDto)
                                  .build();
    }

    @Transactional
    public SingleArticleResDto updateArticle(UpdateArticleReqDto updateArticleReqDto, String slug) {
        UserEntity userEntity = this.lookupService.getCurrentUserEntity()
                                                  .orElseThrow(() -> new RuntimeException("로그인 유저가 아닙니다."));

        ArticleEntity articleEntity = this.articleRepository.findBySlugUsingFetch(slug)
                                                            .orElseThrow(() -> new RuntimeException("해당 게시글이 없습니다."));

        if (articleEntity.getUserEntity()
                         .getUid()
                         .longValue() != userEntity.getUid()
                                                   .longValue()) {
            throw new RuntimeException("작성자만 수정 가능합니다.");
        }

        articleEntity.updateArticle(updateArticleReqDto.getDescription(), updateArticleReqDto.getBody());

        String title = updateArticleReqDto.getTitle();
        if (title != null && !title.isEmpty()) {
            String reNewSlug = this.makeSlug(title);
            articleEntity.updateTitleAndSlug(title, reNewSlug);
        }

        List<String> tagList = new ArrayList<>();
        articleEntity.getArticleTagEntities()
                     .forEach(articleTagEntity -> {
                         tagList.add(articleTagEntity.getTagEntity()
                                                     .getTag());
                     });

        ProfileDto profileDto = createProfileDto(userEntity);
        return SingleArticleResDto.builder()
                                  .slug(slug)
                                  .title(articleEntity.getTitle())
                                  .description(articleEntity.getDescription())
                                  .body(articleEntity.getBody())
                                  .tagList(tagList)
                                  .createdAt(articleEntity.getCreatedAt()
                                                          .toString())
                                  .updatedAt(articleEntity.getUpdatedAt()
                                                          .toString())
                                  .favorite(false)
                                  .favoritesCount(0)
                                  .author(profileDto)
                                  .build();
    }

    public MultipleArticleResDto getArticle(String author, String tag, String favorited, Integer limit,
        Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit);
        Page<ArticleEntity> articles = this.articleRepository.getArticle(author, pageRequest);
        List<ArticleEntity> content = articles.getContent();
        content.forEach(articleEntity -> {
            articleEntity.getArticleTagEntities().for
        });
    }

    private ProfileDto createProfileDto(UserEntity userEntity) {
        return ProfileDto.builder()
                         .username(userEntity.getUsername())
                         .bio(userEntity.getBio())
                         .image(userEntity.getImage())
                         .following(false)
                         .build();
    }

    private String makeSlug(String title) {
        String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
        String result = title.replaceAll(match, " ")
                             .replace(" ", "-")
                             .toLowerCase();

        Optional<ArticleEntity> findBySlug = this.articleRepository.findBySlug(result);
        return findBySlug.isEmpty() ? result : result + "-" + UUID.randomUUID();
    }
}
