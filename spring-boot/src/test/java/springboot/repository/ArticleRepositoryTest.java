package springboot.repository;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.domain.entity.UserEntity;

@SpringBootTest
class ArticleRepositoryTest {
    @Autowired ArticleRepository articleRepository;
    @Autowired UserRepository userRepository;

    @Test
    public void 테스트() {
        Optional<UserEntity> a = this.userRepository.findByUsername("A");
        this.articleRepository.findByUserEntity(a.get());
    }
}