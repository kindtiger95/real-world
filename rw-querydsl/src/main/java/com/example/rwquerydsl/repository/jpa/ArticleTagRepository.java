package com.example.rwquerydsl.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.rwquerydsl.domain.entity.ArticleTagEntity;

public interface ArticleTagRepository extends JpaRepository<ArticleTagEntity, Long> {

    @Query("SELECT at FROM ArticleTagEntity AS at JOIN FETCH at.tagEntity AS t WHERE at.articleId = :articleId")
    List<ArticleTagEntity> findTag(@Param("articleId") Long articleId);
}
