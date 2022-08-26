package springboot.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "Comments")
public class CommentEntity {
    public CommentEntity() {}

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Lob
    private String body;

    @ManyToOne
    @JoinColumn(name = "article_uid")
    private ArticleEntity articleEntity;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    private UserEntity userEntity;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
