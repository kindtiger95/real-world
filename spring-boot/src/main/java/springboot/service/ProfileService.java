package springboot.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.dto.response.ProfileResDto;
import springboot.domain.entity.UserEntity;
import springboot.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final LookupService lookupService;
    private final UserRepository userRepository;

/*    public ProfileResDto getProfile(String username) {
        Optional<UserEntity> currentUserEntity = this.lookupService.getCurrentUserEntity();
        if (currentUserEntity.isEmpty()) {
            Optional<UserEntity> userByUsername = this.userRepository.findByUsername(username);
            UserEntity userEntity = userByUsername.orElseThrow(() -> new RuntimeException("그런 유저 없음"));
            return ProfileResDto.builder()
                                .bio(userEntity.getBio())
                                .image(userEntity.getImage())
                                .following(false)
                                .username(userEntity.getUsername())
                                .build();
        } else {
            Optional<UserEntity> userByFollower = this.userRepository.findUserEntityFollower(username,
                currentUserEntity.get()
                                 .getUid());
            return ProfileResDto.builder()
                                .bio(userEntity.getBio())
                                .image(userEntity.getImage())
                                .following(false)
                                .username(userEntity.getUsername())
                                .build()
        }
    }*/

    public ProfileResDto followingUser(ProfileReq)
}
