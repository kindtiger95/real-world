package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.domain.entity.ArticleEntity;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

}
