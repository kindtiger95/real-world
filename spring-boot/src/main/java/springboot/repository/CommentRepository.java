package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.domain.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
