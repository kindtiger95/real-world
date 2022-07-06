package springboot.database.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.database.entities.UserEntity;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager entitymanager;

    public UserEntity save(UserEntity userEntity) {
        entitymanager.persist(userEntity);
        return userEntity;
    }

    public Optional<UserEntity> findById(Long id) {
        UserEntity userEntity = entitymanager.find(UserEntity.class, id);
        return Optional.ofNullable(userEntity);
    }

    @Transactional
    public UserEntity updateById(Long id, UserEntity updateEntity) throws Exception {
        UserEntity userEntity = this.findById(id).orElseThrow(() -> new Exception("error!"));
        if (updateEntity.)

    }
}
