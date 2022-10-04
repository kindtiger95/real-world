package springboot.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.entity.ArticleEntity;
import springboot.domain.entity.ArticleTagEntity;
import springboot.domain.entity.FavoriteEntity;
import springboot.domain.entity.UserEntity;

@SpringBootTest
class ArticleRepositoryTest {
    @Autowired ArticleRepository articleRepository;
    @Autowired UserRepository userRepository;

    @Test
    @Transactional(readOnly = true)
    public void 테스트3() {
        UserEntity userEntity = this.userRepository.findByUsername("A").get();
        List<ArticleEntity> byUserEntity = this.articleRepository.findAll();
        byUserEntity.forEach(articleEntity -> {
            articleEntity.getArticleTagEntities().forEach(articleTagEntity -> {
                System.out.println(articleTagEntity.getTagEntity().getTag());
            });
            articleEntity.getFavoriteEntities().forEach(favoriteEntity -> {
                System.out.println(favoriteEntity.getUserEntity().getUsername());
            });
        });
    }
}