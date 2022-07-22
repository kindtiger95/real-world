package springboot.database.repositories;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;
import springboot.database.entities.UsersEntity;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager entitymanager;

    public void save(UsersEntity userEntity) {
        entitymanager.persist(userEntity);
    }

    public Optional<UsersEntity> findById(Long id) {
        UsersEntity userEntity = entitymanager.find(UsersEntity.class, id);
        return Optional.ofNullable(userEntity);
    }

    public Optional<UsersEntity> findByUsername(String username) {
        String jpql = "SELECT u FROM users u WHERE u.username = :username";
        List<UsersEntity> resultList = entitymanager.createQuery(jpql, UsersEntity.class)
                                                    .setParameter("username", username)
                                                    .getResultList();
        return resultList.stream().findAny();
    }

    public Optional<UsersEntity> findByEmail(String email) {
        String jpql = "SELECT u FROM users u WHERE u.email = :email";
        List<UsersEntity> resultList = entitymanager.createQuery(jpql, UsersEntity.class)
                                                   .setParameter("email", email)
                                                   .getResultList();
        return resultList.stream().findAny();
    }
}
