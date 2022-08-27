package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.domain.entity.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

}
