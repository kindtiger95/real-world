package springboot.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.FavoriteEntity;
import springboot.domain.entity.UserEntity;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    Optional<FavoriteEntity> findByUserEntityAndArticleEntity(UserEntity userEntity, ArticleEntity articleEntity);

    @Query("SELECT f FROM FavoriteEntity AS f JOIN f.userEntity AS u JOIN f.articleEntity AS a WHERE u.username = :username ORDER BY a.createdAt DESC")
    Page<FavoriteEntity> findUsingUser(@Param("username") String username, Pageable pageable);
}
