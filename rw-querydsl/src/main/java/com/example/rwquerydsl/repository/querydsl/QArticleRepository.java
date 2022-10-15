package com.example.rwquerydsl.repository.querydsl;

import static com.example.rwquerydsl.domain.entity.QArticleEntity.*;
import static com.example.rwquerydsl.domain.entity.QUserEntity.*;

import com.example.rwquerydsl.domain.entity.ArticleEntity;
import com.example.rwquerydsl.domain.entity.QArticleEntity;
import com.example.rwquerydsl.domain.entity.QUserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QArticleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    //     @Query("SELECT a FROM ArticleEntity AS a JOIN FETCH a.userEntity AS u WHERE a.slug = :slug")
    Optional<ArticleEntity> findBySlug(String slug) {
        jpaQueryFactory.selectFrom(articleEntity)
            .join(userEntity).fetchJoin()
            .where(articleEntity.slug.eq(slug))
            .fetch
    }

}
