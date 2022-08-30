package springboot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springboot.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT u FROM UserEntity AS u JOIN FETCH u.followeeEntity AS fe "
        + "JOIN FETCH fe.followerEntity AS fr "
        + "WHERE u.username = :username AND fr.uid = :uid")
    Optional<UserEntity> findUserEntityFollower(@Param("username") String username, @Param("uid") Long uid);

}
