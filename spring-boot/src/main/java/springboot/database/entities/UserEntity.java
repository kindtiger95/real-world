package springboot.database.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserEntity {
    public UserEntity() {}

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
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;
}
