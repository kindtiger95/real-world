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
import org.springframework.util.StringUtils;
import springboot.domain.dto.ArticleDto.ArticleResDto;
import springboot.domain.dto.ArticleDto.AuthorDto;
import springboot.domain.dto.ArticleDto.CreateArticleReqDto;
import springboot.domain.dto.ArticleDto.MultipleArticleResDto;
import springboot.domain.dto.ArticleDto.SingleArticleResDto;
import springboot.domain.dto.ArticleDto.UpdateArticleReqDto;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.FavoriteEntity;
import springboot.domain.entity.UserEntity;
import springboot.repository.ArticleRepository;
import springboot.repository.FavoriteRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;
    private final LookupService lookupService;
    private final TagService tagService;

    @Transactional
    public SingleArticleResDto createArticle(CreateArticleReqDto createArticleReqDto) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 유저가 아닙니다."));
        String slug = this.makeSlug(createArticleReqDto.getTitle());
        ArticleEntity articleEntity = ArticleEntity.builder()
                                                   .title(createArticleReqDto.getTitle())
                                                   .body(createArticleReqDto.getBody())
                                                   .slug(slug)
                                                   .description(createArticleReqDto.getDescription())
                                                   .userEntity(loginUser)
                                                   .build();
        this.articleRepository.save(articleEntity);
        this.tagService.createTag(createArticleReqDto.getTagList(), articleEntity);
        AuthorDto authorDto = AuthorDto.createAuthorDto(loginUser);
        ArticleResDto articleResDto = ArticleResDto.builder()
                                                   .slug(slug)
                                                   .title(articleEntity.getTitle())
                                                   .description(articleEntity.getDescription())
                                                   .body(articleEntity.getBody())
                                                   .tagList(createArticleReqDto.getTagList())
                                                   .createdAt(articleEntity.getCreatedAt().toString())
                                                   .updatedAt(articleEntity.getUpdatedAt().toString())
                                                   .favorite(false)
                                                   .favoritesCount(0)
                                                   .author(authorDto)
                                                   .build();

        return new SingleArticleResDto(articleResDto);
    }

    @Transactional
    public SingleArticleResDto updateArticle(UpdateArticleReqDto updateArticleReqDto, String slug) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 유저가 아닙니다."));
        ArticleEntity articleEntity = this.articleRepository.findBySlugUsingFetch(slug).orElseThrow(() -> new RuntimeException("해당 게시글이 없습니다."));

        if (articleEntity.getUserEntity().getUid() != loginUser.getUid().longValue())
            throw new RuntimeException("작성자만 수정 가능합니다.");

        articleEntity.updateArticle(updateArticleReqDto.getDescription(), updateArticleReqDto.getBody());

        String title = updateArticleReqDto.getTitle();
        if (StringUtils.hasText(title)) {
            String reNewSlug = this.makeSlug(title);
            articleEntity.updateTitleAndSlug(title, reNewSlug);
        }

        // @formatter:off
        List<String> tagList = new ArrayList<>();
        articleEntity.getArticleTagEntities().forEach(articleTagEntity -> {
                         tagList.add(articleTagEntity.getTagEntity().getTag());
                     });

        AuthorDto authorDto = AuthorDto.createAuthorDto(loginUser);
        ArticleResDto articleResDto = ArticleResDto.builder()
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
        return new SingleArticleResDto(articleResDto);
        // @formatter:on
    }

    @Transactional
    public void deleteArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlugFetchUser(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        if (loginUser != articleEntity.getUserEntity())
            throw new RuntimeException("권한 없음");
        this.articleRepository.delete(articleEntity);
    }

    public MultipleArticleResDto getArticles(String author, String tag, String favorited, Integer limit, Integer offset) {
        if (StringUtils.hasText(author))
            return this.getArticleByAuthor(author, limit, offset);
        else if (StringUtils.hasText(tag))
            return this.getArticleByTag(tag, limit, offset);
        else if (StringUtils.hasText(favorited))
            return this.getArticleByFavorite(favorited, limit, offset);

        throw new RuntimeException("파라미터 오류");
    }

    public SingleArticleResDto getArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlugUsingFetch(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElse(null);
        List<String> tagList = new ArrayList<>();
        articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
        ArticleResDto articleResDto = getSingleArticleResDto(loginUser, articleEntity, tagList);
        return new SingleArticleResDto(articleResDto);
    }

    public MultipleArticleResDto getArticlesFeed(Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("현재 유저를 찾을 수 없습니다."));
        Page<ArticleEntity> articleByFollowerUser = this.articleRepository.getArticleByFollowerUser(loginUser, pageRequest);
        List<ArticleEntity> content = articleByFollowerUser.getContent();
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(articleEntity -> {
            List<String> tagList = new ArrayList<>();
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
            ArticleResDto articleResDto = getSingleArticleResDto(loginUser, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(articleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    @Transactional
    public SingleArticleResDto favoriteArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlugUsingFetch(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다"));
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 정보를 찾을 수 없습니다"));
        FavoriteEntity favoriteEntity = FavoriteEntity.builder()
                                                      .userEntity(loginUser)
                                                      .articleEntity(articleEntity)
                                                      .build();
        this.favoriteRepository.save(favoriteEntity);
        List<String> tagList = new ArrayList<>();
        articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
        ArticleResDto articleResDto = getSingleArticleResDto(loginUser, articleEntity, tagList);
        return new SingleArticleResDto(articleResDto);
    }

    @Transactional
    public void unFavoriteArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 정보를 찾을 수 없습니다"));
        FavoriteEntity favoriteEntity = this.favoriteRepository.findByUserEntityAndArticleEntity(loginUser, articleEntity).orElseThrow(() -> new RuntimeException("팔로우 하지 않음"));
        this.favoriteRepository.delete(favoriteEntity);
    }

    private MultipleArticleResDto getArticleByAuthor(String author, Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleEntity> articleByAuthorPaging = this.articleRepository.findUsingAuthorPaging(author, pageRequest);
        List<ArticleEntity> content = articleByAuthorPaging.getContent();
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElse(null);
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(articleEntity -> {
            List<String> tagList = new ArrayList<>();
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
            ArticleResDto articleResDto = getSingleArticleResDto(loginUser, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(articleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    private MultipleArticleResDto getArticleByTag(String tag, Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleEntity> usingTagPaging = this.articleRepository.findUsingTagPaging(tag, pageRequest);
        List<ArticleEntity> content = usingTagPaging.getContent();
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElse(null);
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(articleEntity -> {
            List<String> tagList = new ArrayList<>();
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
            ArticleResDto articleResDto = getSingleArticleResDto(loginUser, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(articleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    private MultipleArticleResDto getArticleByFavorite(String favoriteUser, Integer limit, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FavoriteEntity> favoriteByUser = this.favoriteRepository.findUsingUser(favoriteUser, pageRequest);
        List<FavoriteEntity> content = favoriteByUser.getContent();
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElse(null);
        MultipleArticleResDto multipleArticleResDto = new MultipleArticleResDto();
        content.forEach(favoriteEntity -> {
            List<String> tagList = new ArrayList<>();
            ArticleEntity articleEntity = favoriteEntity.getArticleEntity();
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
            ArticleResDto articleResDto = getSingleArticleResDto(loginUser, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(articleResDto);
        });
        multipleArticleResDto.setArticlesCount(multipleArticleResDto.getArticles().size());
        return multipleArticleResDto;
    }

    private ArticleResDto getSingleArticleResDto(UserEntity currentUserEntity, ArticleEntity articleEntity, List<String> tagList) {
        List<FavoriteEntity> favoriteEntities = articleEntity.getFavoriteEntities();
        Integer favoriteCount = favoriteEntities.size();

        // @formatter:off
        boolean isFavorite = currentUserEntity != null && favoriteEntities.stream()
                                                                          .anyMatch(favoriteEntity -> favoriteEntity.getUserEntity().getUid() == currentUserEntity.getUid().longValue());
        boolean isFollowing = currentUserEntity != null && articleEntity.getUserEntity().getFolloweeEntities()
                                                                        .stream().anyMatch(followEntity -> followEntity.getFollowerEntity().getUid() == currentUserEntity.getUid().longValue());

        return this.createSingleArticleResDto(articleEntity, tagList, isFavorite, isFollowing, favoriteCount);
    }
    // @formatter:on


    // @formatter:off
    private ArticleResDto createSingleArticleResDto(ArticleEntity articleEntity, List<String> tagList, boolean isFavorite, boolean isFollowing, Integer favoriteCount) {
        AuthorDto authorDto = AuthorDto.createAuthorDto(articleEntity.getUserEntity(), isFollowing);
        return ArticleResDto.builder()
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

    private String makeSlug(String title) {
        String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
        String result = title.replaceAll(match, " ")
                             .replace(" ", "-")
                             .toLowerCase();

        Optional<ArticleEntity> findBySlug = this.articleRepository.findBySlug(result);
        return findBySlug.isEmpty() ? result : result + "-" + UUID.randomUUID();
    }


}
