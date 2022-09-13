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
import springboot.domain.dto.ArticleDto.AuthorDto;
import springboot.domain.dto.ArticleDto.CreateArticleReqDto;
import springboot.domain.dto.ArticleDto.MultipleArticleResDto;
import springboot.domain.dto.ArticleDto.SingleArticleResDto;
import springboot.domain.dto.ArticleDto.UpdateArticleReqDto;
import springboot.domain.dto.ProfileDto;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.UserEntity;
import springboot.repository.ArticleRepository;
import springboot.repository.FollowRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;
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

        AuthorDto authorDto = createAuthorDto(userEntity);
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
                                  .author(authorDto)
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

        AuthorDto authorDto = createAuthorDto(userEntity);
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
                                  .author(authorDto)
                                  .build();
    }

    public MultipleArticleResDto getArticle(String author, String tag, String favorited, Integer limit, Integer offset) {
        if (author != null) {
            return this.getArticleByAuthor(author, limit, offset);
        }
        return null;
    }

//    public MultipleArticleResDto getArticleFeed(Integer limit, Integer offset) {
//        UserEntity userEntity = this.lookupService.getCurrentUserEntity()
//                                                  .orElseThrow(() -> new RuntimeException("현재 유저를 찾을 수 없습니다."));
//
//
//    }

    private MultipleArticleResDto getArticleByAuthor(String author, Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleEntity> articleByAuthorPaging = this.articleRepository.findArticleByAuthorPaging(author, pageRequest);
        List<ArticleEntity> content = articleByAuthorPaging.getContent();
        Optional<UserEntity> currentUserEntityOpt = this.lookupService.getCurrentUserEntity();
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(articleEntity -> {
            List<String> tagList = new ArrayList<>();
            articleEntity.getArticleTagEntities()
                         .forEach(articleTagEntity -> {
                             tagList.add(articleTagEntity.getTagEntity()
                                                         .getTag());
                         });
            boolean isFavorite = false;
            if (currentUserEntityOpt.isPresent()) {
                isFavorite = articleEntity.getFavoriteEntities()
                                          .stream()
                                          .anyMatch(favoriteEntity -> favoriteEntity.getUserEntity()
                                                                                    .getUid()
                                                                                    .longValue() == currentUserEntityOpt.get()
                                                                                                                        .getUid()
                                                                                                                        .longValue());
            }

            AuthorDto authorDto = createAuthorDto(articleEntity.getUserEntity());
            SingleArticleResDto singleArticleResDto = SingleArticleResDto.builder()
                                                           .slug(articleEntity.getSlug())
                                                           .title(articleEntity.getTitle())
                                                           .description(articleEntity.getDescription())
                                                           .body(articleEntity.getBody())
                                                           .tagList(tagList)
                                                           .createdAt(articleEntity.getCreatedAt()
                                                                                   .toString())
                                                           .updatedAt(articleEntity.getUpdatedAt()
                                                                                   .toString())
                                                           .favorite(isFavorite)
                                                           .favoritesCount(articleEntity.getFavoriteEntities()
                                                                                        .size())
                                                           .author(authorDto)
                                                           .build();
            multipleArticleResDto.getArticles().add(singleArticleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    private AuthorDto createAuthorDto(UserEntity userEntity) {
        return AuthorDto.builder()
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
