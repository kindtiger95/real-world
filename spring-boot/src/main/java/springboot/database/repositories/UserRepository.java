package springboot.database.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.database.entities.UserEntity;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager entitymanager;

    public void save(UserEntity userEntity) {
        entitymanager.persist(userEntity);
    }

    public Optional<UserEntity> findById(Long id) {
        UserEntity userEntity = entitymanager.find(UserEntity.class, id);
        return Optional.ofNullable(userEntity);
    }

    public Optional<UserEntity> findByUsername(String username) {
        String jpql = "SELECT u FROM user u WHERE u.username = :username";
        List<UserEntity> resultList = entitymanager.createQuery(jpql, UserEntity.class)
                                                   .setParameter("username", username)
                                                   .getResultList();
        return resultList.stream().findAny();
    }

    public Optional<UserEntity> findByEmail(String email) {
        String jpql = "SELECT u FROM user u WHERE u.email = :email";
        List<UserEntity> resultList = entitymanager.createQuery(jpql, UserEntity.class)
                                                   .setParameter("email", email)
                                                   .getResultList();
        return resultList.stream().findAny();
    }
}
