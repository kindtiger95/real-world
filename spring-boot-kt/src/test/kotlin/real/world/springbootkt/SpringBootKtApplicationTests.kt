package real.world.springbootkt

import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import jakarta.persistence.PersistenceUnit
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import real.world.springbootkt.domain.user.User

@ActiveProfiles("test")
@SpringBootTest
class SpringBootKtApplicationTests @Autowired constructor(private val emf: EntityManagerFactory) {

    @Test
    fun springBootTest() {
        // persistence.xml 설정이 없기 때문에 먹지 않음
        // val emf = Persistence.createEntityManagerFactory("master")
        println("entityManagerFactory is open: ${emf.isOpen}")
        val em = emf.createEntityManager()
        val tr = em.transaction
        try {
            tr.begin()
            val user = User("test@email", "luke", "1234")
            em.persist(user)
            tr.commit()
        } catch (e: Exception) {
            println(e.message)
            tr.rollback()
        } finally {
            em.close()
        }
    }
}
