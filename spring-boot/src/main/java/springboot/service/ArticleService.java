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
import springboot.domain.dto.CommentDto.CommentResDto;
import springboot.domain.dto.CommentDto.CreateCommentReqDto;
import springboot.domain.dto.CommentDto.MultipleCommentDto;
import springboot.domain.dto.CommentDto.SingleCommentDto;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.CommentEntity;
import springboot.domain.entity.FavoriteEntity;
import springboot.domain.entity.UserEntity;
import springboot.repository.ArticleRepository;
import springboot.repository.ArticleTagRepository;
import springboot.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
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
        ArticleResDto articleResDto = ArticleResDto.builder()
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

        return new SingleArticleResDto(articleResDto);
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
        ArticleEntity articleEntity = this.articleRepository.findBySlugFetchUser(slug)
                                                            .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity currentUser = this.lookupService.getCurrentUserEntity()
                                                  .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        UserEntity userEntity = articleEntity.getUserEntity();
        if (currentUser != userEntity) {
            throw new RuntimeException("권한 없음");
        }
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

    @Transactional
    public SingleArticleResDto favoriteArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlugUsingFetch(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다"));
        UserEntity currentUserEntity = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 정보를 찾을 수 없습니다"));
        FavoriteEntity favoriteEntity = FavoriteEntity.builder()
                                                      .userEntity(currentUserEntity)
                                                      .articleEntity(articleEntity)
                                                      .build();
        this.favoriteRepository.save(favoriteEntity);
        List<String> tagList = new ArrayList<>();
        articleEntity.getArticleTagEntities().forEach(articleTagEntity -> tagList.add(articleTagEntity.getTagEntity().getTag()));
        ArticleResDto articleResDto = getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
        return new SingleArticleResDto(articleResDto);
    }

    @Transactional
    public void unFavoriteArticle(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        UserEntity userEntity = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 정보를 찾을 수 없습니다"));
        FavoriteEntity favoriteEntity = this.favoriteRepository.findByUserEntityAndArticleEntity(userEntity, articleEntity).orElseThrow(() -> new RuntimeException("팔로우 하지 않음"));
        this.favoriteRepository.delete(favoriteEntity);
    }

    @Transactional
    public SingleCommentDto addComment(String slug, CreateCommentReqDto createCommentReqDto) {
        UserEntity userEntity = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 유저만 댓글을 달 수 있습니다."));
        ArticleEntity articleEntity = this.articleRepository.findBySlugFetchUser(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        boolean isFollowing = articleEntity.getUserEntity().getFolloweeEntities().stream().anyMatch(followEntity -> followEntity.getFolloweeEntity() == userEntity);
        AuthorDto authorDto = AuthorDto.builder()
                                       .bio(userEntity.getBio())
                                       .username(userEntity.getUsername())
                                       .following(isFollowing)
                                       .image(userEntity.getImage())
                                       .build();
        CommentEntity commentEntity = CommentEntity.builder()
                                                   .userEntity(userEntity)
                                                   .articleEntity(articleEntity)
                                                   .body(createCommentReqDto.getBody())
                                                   .build();
        this.commentRepository.save(commentEntity);
        CommentResDto commentResDto = CommentResDto.builder()
                                                   .id(commentEntity.getUid())
                                                   .createdAt(commentEntity.getCreatedAt())
                                                   .updatedAt(commentEntity.getUpdatedAt())
                                                   .body(commentEntity.getBody())
                                                   .author(authorDto)
                                                   .build();
        return SingleCommentDto.builder()
                               .comment(commentResDto)
                               .build();
    }

    public MultipleCommentDto getComments(String slug) {
        ArticleEntity articleEntity = this.articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        Optional<UserEntity> currentUserEntity = this.lookupService.getCurrentUserEntity();
        List<CommentEntity> commentEntityList = this.commentRepository.findByArticleEntity(articleEntity.getUid());
        MultipleCommentDto multipleCommentDto = new MultipleCommentDto();
        for (CommentEntity commentEntity : commentEntityList) {
            UserEntity userEntity = commentEntity.getUserEntity();
            boolean isFollowing = currentUserEntity.isPresent() && userEntity.getFolloweeEntities()
                                                                             .stream()
                                                                             .anyMatch(followEntity -> followEntity.getFollowerEntity() == currentUserEntity.get());
            AuthorDto authorDto = AuthorDto.builder()
                                           .image(userEntity.getImage())
                                           .bio(userEntity.getBio())
                                           .username(userEntity.getUsername())
                                           .following(isFollowing)
                                           .build();
            CommentResDto commentResDto = CommentResDto.builder()
                                                       .id(commentEntity.getUid())
                                                       .createdAt(commentEntity.getCreatedAt())
                                                       .updatedAt(commentEntity.getUpdatedAt())
                                                       .body(commentEntity.getBody())
                                                       .author(authorDto)
                                                       .build();
            multipleCommentDto.getComments().add(commentResDto);
        }
        return multipleCommentDto;
    }

    public void deleteComment(String slug, Long id) {
        UserEntity userEntity = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 정보가 없습니다."));
        ArticleEntity articleEntity = this.articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        CommentEntity commentEntity = this.commentRepository.findByIdFetchJoin(id).orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));
        if (commentEntity.getUserEntity() != userEntity || commentEntity.getArticleEntity() != articleEntity)
            throw new RuntimeException("권한이 없습니다.");
        this.commentRepository.delete(commentEntity);
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
            ArticleResDto articleResDto = getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(articleResDto);
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
            ArticleResDto articleResDto = getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
            multipleArticleResDto.getArticles().add(articleResDto);
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
            ArticleResDto articleResDto = getSingleArticleResDto(currentUserEntity, articleEntity, tagList);
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
        AuthorDto authorDto = this.createAuthorDto(articleEntity.getUserEntity(), isFollowing);
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
