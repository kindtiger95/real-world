package springboot.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.dto.ArticleDto.AuthorDto;
import springboot.domain.dto.CommentDto.CommentResDto;
import springboot.domain.dto.CommentDto.CreateCommentReqDto;
import springboot.domain.dto.CommentDto.MultipleCommentDto;
import springboot.domain.dto.CommentDto.SingleCommentDto;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.CommentEntity;
import springboot.domain.entity.UserEntity;
import springboot.repository.ArticleRepository;
import springboot.repository.CommentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final LookupService lookupService;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public SingleCommentDto addComment(String slug, CreateCommentReqDto createCommentReqDto) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 유저만 댓글을 달 수 있습니다."));
        ArticleEntity articleEntity = this.articleRepository.findBySlugFetchUser(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        boolean isFollowing = articleEntity.getUserEntity().getFolloweeEntities().stream().anyMatch(followEntity -> followEntity.getFolloweeEntity() == loginUser);
        AuthorDto authorDto = AuthorDto.builder()
                                       .bio(loginUser.getBio())
                                       .username(loginUser.getUsername())
                                       .following(isFollowing)
                                       .image(loginUser.getImage())
                                       .build();
        CommentEntity commentEntity = CommentEntity.builder()
                                                   .userEntity(loginUser)
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
        Optional<UserEntity> loginUser = this.lookupService.getCurrentUserEntity();
        List<CommentEntity> commentEntityList = this.commentRepository.findByArticleEntity(articleEntity.getUid());
        MultipleCommentDto multipleCommentDto = new MultipleCommentDto();
        for (CommentEntity commentEntity : commentEntityList) {
            UserEntity userEntity = commentEntity.getUserEntity();
            boolean isFollowing = loginUser.isPresent() && userEntity.getFolloweeEntities()
                                                                             .stream()
                                                                             .anyMatch(followEntity -> followEntity.getFollowerEntity() == loginUser.get());
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

    @Transactional
    public void deleteComment(String slug, Long id) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("로그인 정보가 없습니다."));
        ArticleEntity articleEntity = this.articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        CommentEntity commentEntity = this.commentRepository.findByIdFetchJoin(id).orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));
        if (commentEntity.getUserEntity() != loginUser || commentEntity.getArticleEntity() != articleEntity)
            throw new RuntimeException("권한이 없습니다.");
        this.commentRepository.delete(commentEntity);
    }
}
