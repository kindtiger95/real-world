package springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springboot.domain.dto.ArticleDto.AuthorDto;
import springboot.domain.dto.ArticleDto.CreateArticleReqDto;
import springboot.domain.dto.ArticleDto.MultipleArticleResDto;
import springboot.domain.dto.ArticleDto.SingleArticleResDto;
import springboot.domain.dto.ArticleDto.UpdateArticleReqDto;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.FavoriteEntity;
import springboot.domain.entity.UserEntity;
import springboot.repository.ArticleRepository;
import springboot.repository.ArticleTagRepository;
import springboot.repository.FavoriteRepository;
import springboot.repository.FollowRepository;
import springboot.repository.TagRepository;
import springboot.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;
    private final ArticleTagRepository articleTagRepository;
    private final FavoriteRepository favoriteRepository;
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

        // @formatter:off
        List<String> tagList = new ArrayList<>();
        articleEntity.getArticleTagEntities()
                     .forEach(articleTagEntity -> {
                         tagList.add(articleTagEntity.getTagEntity().getTag());
                     });

        AuthorDto authorDto = createAuthorDto(userEntity);
        return SingleArticleResDto.builder()
                                  .slug(slug)
                                  .title(articleEntity.getTitle())
                                  .description(articleEntity.getDescription())
                                  .body(articleEntity.getBody())
                                  .tagList(tagList)
                                  .createdAt(articleEntity.getCreatedAt().toString())
                                  .updatedAt(articleEntity.getUpdatedAt().toString())
                                  .favorite(false)
                                  .favoritesCount(0)
                                  .author(authorDto)
                                  .build();
        // @formatter:on
    }

    public void deleteArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlug(slug)
                                                            .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity currentUser = this.lookupService.getCurrentUserEntity()
                                                  .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        UserEntity userEntity = articleEntity.getUserEntity();
        if (currentUser != userEntity)
            throw new RuntimeException("권한 없음");
        this.articleRepository.delete(articleEntity);
    }

    public MultipleArticleResDto getArticle(String author, String tag, String favorited, Integer limit, Integer offset) {
        if (StringUtils.hasText(author)) {
            return this.getArticleByAuthor(author, limit, offset);
        } else if (StringUtils.hasText(tag)) {
            return this.getArticleByTag(tag, limit, offset);
        } else if (StringUtils.hasText(favorited)) {
            return this.getArticleByFavorite(favorited, limit, offset);
        }
        throw new RuntimeException("파라미터 오류");
    }

//    public MultipleArticleResDto getArticleFeed(Integer limit, Integer offset) {
//        UserEntity userEntity = this.lookupService.getCurrentUserEntity()
//                                                  .orElseThrow(() -> new RuntimeException("현재 유저를 찾을 수 없습니다."));
//
//
//    }

    public SingleArticleResDto favoriteArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다"));
        UserEntity currentUserEntity = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 정보를 찾을 수 없습니다"));
        FavoriteEntity favoriteEntity = FavoriteEntity.builder()
                                                      .userEntity(currentUserEntity)
                                                      .articleEntity(articleEntity)
                                                      .build();
        this.favoriteRepository.save(favoriteEntity);
        List<String> tagList = new ArrayList<>();
        articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
        return getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
    }

    private MultipleArticleResDto getArticleByAuthor(String author, Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleEntity> articleByAuthorPaging = this.articleRepository.findUsingAuthorPaging(author, pageRequest);
        List<ArticleEntity> content = articleByAuthorPaging.getContent();
        UserEntity currentUserEntity = this.lookupService.getCurrentUserEntity().orElse(null);
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(articleEntity -> {
            List<String> tagList = new ArrayList<>();
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
            SingleArticleResDto singleArticleResDto = getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(singleArticleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    private MultipleArticleResDto getArticleByTag(String tag, Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleEntity> usingTagPaging = this.articleRepository.findUsingTagPaging(tag, pageRequest);
        List<ArticleEntity> content = usingTagPaging.getContent();
        UserEntity currentUserEntity = this.lookupService.getCurrentUserEntity().orElse(null);
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(articleEntity -> {
            List<String> tagList = new ArrayList<>();
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
            SingleArticleResDto singleArticleResDto = getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(singleArticleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    private MultipleArticleResDto getArticleByFavorite(String favoriteUser, Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FavoriteEntity> favoriteByUser = this.favoriteRepository.findUsingUser(favoriteUser, pageRequest);
        List<FavoriteEntity> content = favoriteByUser.getContent();
        UserEntity currentUserEntity = this.lookupService.getCurrentUserEntity().orElse(null);
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(favoriteEntity -> {
            List<String> tagList = new ArrayList<>();
            ArticleEntity articleEntity = favoriteEntity.getArticleEntity();
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
            SingleArticleResDto singleArticleResDto = getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(singleArticleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    private SingleArticleResDto getSingleArticleResDto(UserEntity currentUserEntity, ArticleEntity articleEntity, List<String> tagList) {
        List<FavoriteEntity> favoriteEntities = articleEntity.getFavoriteEntities();
        Integer favoriteCount = favoriteEntities.size();

        // @formatter:off
        boolean isFavorite = currentUserEntity != null && favoriteEntities.stream()
                                                                          .anyMatch(favoriteEntity -> favoriteEntity.getUserEntity().getUid() == currentUserEntity.getUid().longValue());
        boolean isFollowing = currentUserEntity != null && articleEntity.getUserEntity().getFollowerEntity()
                                                                        .stream().anyMatch(followEntity -> followEntity.getFollowerEntity().getUid() == currentUserEntity.getUid().longValue());

        return this.createSingleArticleResDto(articleEntity, tagList, isFavorite, isFollowing, favoriteCount);
    }
    // @formatter:on


    // @formatter:off
    private SingleArticleResDto createSingleArticleResDto(ArticleEntity articleEntity, List<String> tagList, boolean isFavorite, boolean isFollowing, Integer favoriteCount) {
        AuthorDto authorDto = this.createAuthorDto(articleEntity.getUserEntity(), isFollowing);
        return SingleArticleResDto.builder()
                                  .slug(articleEntity.getSlug())
                                  .title(articleEntity.getTitle())
                                  .description(articleEntity.getDescription())
                                  .body(articleEntity.getBody())
                                  .tagList(tagList)
                                  .createdAt(articleEntity.getCreatedAt().toString())
                                  .updatedAt(articleEntity.getUpdatedAt().toString())
                                  .favorite(isFavorite)
                                  .favoritesCount(favoriteCount)
                                  .author(authorDto)
                                  .build();
    }
    // @formatter:on

    private AuthorDto createAuthorDto(UserEntity userEntity) {
        return AuthorDto.builder()
                        .username(userEntity.getUsername())
                        .bio(userEntity.getBio())
                        .image(userEntity.getImage())
                        .following(false)
                        .build();
    }

    private AuthorDto createAuthorDto(UserEntity userEntity, boolean isFollowing) {
        return AuthorDto.builder()
                        .username(userEntity.getUsername())
                        .bio(userEntity.getBio())
                        .image(userEntity.getImage())
                        .following(isFollowing)
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
