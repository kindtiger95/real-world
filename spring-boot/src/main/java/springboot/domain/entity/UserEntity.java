package springboot.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String bio;
    private String image;

    @Builder.Default
    @OneToMany(mappedBy = "followerEntity")
    List<FollowEntity> followerEntity = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "followeeEntity")
    List<FollowEntity> followeeEntity = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "userEntity")
    List<ArticleEntity> articleEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "userEntity")
    List<FavoriteEntity> favoriteEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "userEntity")
    List<CommentEntity> commentEntities = new ArrayList<>();
}