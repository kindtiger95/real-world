package springboot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
