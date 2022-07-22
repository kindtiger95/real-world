package springboot.database.entities;

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
@Entity(name = "Articles")
public class ArticlesEntity {
    public ArticlesEntity() {}

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String slug;

    private String title;

    private String description;

    @Lob
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    private UsersEntity usersEntity;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
