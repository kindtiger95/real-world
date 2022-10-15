package com.example.rwquerydsl.repository.jpa;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.rwquerydsl.domain.entity.ArticleEntity;
import com.example.rwquerydsl.domain.entity.FavoriteEntity;
import com.example.rwquerydsl.domain.entity.UserEntity;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    Optional<FavoriteEntity> findByUserEntityAndArticleEntity(UserEntity userEntity, ArticleEntity articleEntity);

    @Query(value = "SELECT f FROM FavoriteEntity AS f JOIN FETCH f.articleEntity AS a JOIN f.userEntity AS u ON u.username = :username",
    countQuery = "SELECT COUNT(f) FROM FavoriteEntity AS f JOIN f.userEntity AS u ON u.username = :username")
    Page<FavoriteEntity> findUsingUser(@Param("username") String username, Pageable pageable);
}
