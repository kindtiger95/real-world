package com.example.rwquerydsl.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleEntity extends BaseEntity {

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String title;
    private String description;

    @Lob
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.ALL)
    private List<FavoriteEntity> favoriteEntities = new ArrayList<>();

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities = new ArrayList<>();

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.ALL)
    private List<ArticleTagEntity> articleTagEntities = new ArrayList<>();

    public void updateTitleAndSlug(String title, String slug) {
        this.slug = slug;
        this.title = title;
    }

    public void updateArticle(String description, String body) {
        if (StringUtils.hasText(description))
            this.description = description;

        if (StringUtils.hasText(body))
            this.body = body;
    }
}


