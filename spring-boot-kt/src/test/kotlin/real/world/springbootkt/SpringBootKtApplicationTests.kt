package real.world.springbootkt

import jakarta.persistence.EntityManagerFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import real.world.springbootkt.domain.user.User
import real.world.springbootkt.domain.user.UserRepository

@ActiveProfiles("test")
@SpringBootTest
class SpringBootKtApplicationTests {

}
