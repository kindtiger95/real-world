package springboot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.entity.UserEntity;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void 멤버_저장_테스트() {
        UserEntity user1 = UserEntity.builder()
                                  .username("인범")
                                  .password("1234")
                                  .email("a@a.com")
                                  .build();
        UserEntity user2 = UserEntity.builder()
                                     .username("유경")
                                     .password("1234")
                                     .email("a@a.com")
                                     .build();
        this.userRepository.save(user1);
        this.userRepository.save(user2);

        Optional<UserEntity> userFind1 = this.userRepository.findById(user1.getUid());
        Optional<UserEntity> userFind2 = this.userRepository.findById(user2.getUid());
        assertThat(userFind1.get()).isEqualTo(user1);
        assertThat(userFind2.get()).isEqualTo(user2);
    }
}