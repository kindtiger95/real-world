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

    @Query("SELECT DISTINCT a FROM ArticleEntity AS a "
        + "JOIN FETCH a.userEntity AS u "
        + "JOIN FETCH a.articleTagEntities AS at "
        + "JOIN FETCH at.tagEntity AS t "
        + "WHERE a.slug = :slug")
    Optional<ArticleEntity> findBySlugUsingFetch(@Param("slug") String slug);

    @Query(value = "SELECT DISTINCT a FROM ArticleEntity AS a "
        + "JOIN FETCH a.userEntity AS u "
        + "WHERE u.username = :author",
        countQuery = "SELECT COUNT(u.username) FROM UserEntity AS u")
    Page<ArticleEntity> findArticleByAuthorPaging(@Param("author") String author, Pageable pageable);

    List<ArticleEntity> findByUserEntity(UserEntity userEntity);
}
