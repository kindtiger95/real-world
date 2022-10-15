package com.example.rwquerydsl.repository.jpa;

import com.example.rwquerydsl.domain.entity.CommentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT c FROM CommentEntity AS c JOIN FETCH c.userEntity AS u JOIN ArticleEntity AS a ON a.uid = :uid ORDER BY c.createdAt DESC")
    List<CommentEntity> findByArticleEntity(@Param("uid") Long uid);

    @Query("SELECT c FROM CommentEntity AS c JOIN FETCH c.userEntity JOIN FETCH c.articleEntity WHERE c.uid = :uid")
    Optional<CommentEntity> findByIdFetchJoin(@Param("uid") Long uid);
}
