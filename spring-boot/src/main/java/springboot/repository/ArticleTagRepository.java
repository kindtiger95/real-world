package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.domain.entity.ArticleTagEntity;

public interface ArticleTagRepository extends JpaRepository<ArticleTagEntity, Long> {

}
