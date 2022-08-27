package springboot.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
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

    @OneToMany(mappedBy = "articleEntity")
    private List<FavoriteEntity> favoriteEntities = new ArrayList<>();

    @OneToMany(mappedBy = "articleEntity")
    private List<CommentEntity> commentEntities = new ArrayList<>();

    @OneToMany(mappedBy = "articleEntity")
    private List<ArticleTagEntity> articleTagEntities = new ArrayList<>();
}

