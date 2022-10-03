package springboot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springboot.domain.entity.FollowEntity;
import springboot.domain.entity.UserEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    @Query("SELECT f FROM FollowEntity AS f WHERE f.followeeId = :followeeId AND f.followerId = :followerId")
    Optional<FollowEntity> findExistFollow(@Param("followeeId") Long followeeId, @Param("followerId") Long followerId);

    @Query("SELECT f FROM FollowEntity AS f WHERE f.followerEntity = :followerEntity AND f.followeeEntity = :followeeEntity")
    Optional<FollowEntity> findExistFollowByEntity(@Param("followerEntity") UserEntity followerEntity, @Param("followeeEntity") UserEntity followeeEntity);
}
