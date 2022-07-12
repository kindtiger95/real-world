package springboot.database.entities;

import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity(name = "Users")
public class UsersEntity {
    public UsersEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String bio;

    @Column
    private String image;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
