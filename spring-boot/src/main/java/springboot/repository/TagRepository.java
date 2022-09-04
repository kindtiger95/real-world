package springboot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.domain.entity.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTag(String tag);
}
