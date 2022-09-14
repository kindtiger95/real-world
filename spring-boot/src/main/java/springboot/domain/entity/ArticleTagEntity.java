package springboot.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "article_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTagEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    ArticleEntity articleEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    TagEntity tagEntity;

    @Column(name = "article_id", updatable = false, insertable = false)
    private Long articleId;

    @Column(name = "tag_id", updatable = false, insertable = false)
    private Long tagId;
}
