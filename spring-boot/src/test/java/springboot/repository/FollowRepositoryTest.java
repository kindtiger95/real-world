package springboot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FollowRepositoryTest {
    @Autowired FollowRepository followRepository;

    @Test
    public void 조회_쿼리_확인() {
        this.followRepository.findExistFollow(1L, 2L);
    }
}