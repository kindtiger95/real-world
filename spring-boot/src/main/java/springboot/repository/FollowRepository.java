package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.domain.entity.FollowEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

}
