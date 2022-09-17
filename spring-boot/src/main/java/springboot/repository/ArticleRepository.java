package springboot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.UserEntity;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Optional<ArticleEntity> findBySlug(String slug);

    // 페이징이 필요 없어서 DISTINCT 로 조회
    @Query("SELECT DISTINCT a FROM ArticleEntity AS a "
        + "JOIN FETCH a.userEntity AS u "
        + "JOIN FETCH a.articleTagEntities AS at "
        + "JOIN FETCH at.tagEntity AS t "
        + "WHERE a.slug = :slug")
    Optional<ArticleEntity> findBySlugUsingFetch(@Param("slug") String slug);

    @Query(value = "SELECT a FROM ArticleEntity AS a JOIN FETCH a.userEntity AS u JOIN a.userEntity AS ue WHERE ue.username = :author ORDER BY a.createdAt DESC",
    countQuery = "SELECT COUNT(a.uid) FROM ArticleEntity AS a JOIN a.userEntity AS ue WHERE ue.username = :author")
    Page<ArticleEntity> findUsingAuthorPaging(@Param("author") String author, Pageable pageable);

    Page<ArticleEntity> findByUserEntity(UserEntity userEntity, Pageable pageable);

    List<ArticleEntity> findByUserEntity(UserEntity userEntity);

    @Query(value = "SELECT a FROM ArticleEntity AS a JOIN FETCH a.userEntity AS u JOIN a.articleTagEntities AS at JOIN at.tagEntity AS t WHERE t.tag = :tag ORDER BY a.createdAt DESC",
        countQuery = "SELECT COUNT(a.uid) FROM ArticleEntity AS a JOIN a.articleTagEntities AS at JOIN at.tagEntity AS t WHERE t.tag = :tag")
    Page<ArticleEntity> findUsingTagPaging(@Param("tag") String tag, Pageable pageable);
}
