package springboot.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.dto.ProfileDto;
import springboot.domain.entity.FollowEntity;
import springboot.domain.entity.UserEntity;
import springboot.repository.FollowRepository;
import springboot.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final LookupService lookupService;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public ProfileDto getProfile(String username) {
        boolean isFollow = false;
        Optional<UserEntity> findByUsernameOpt = this.userRepository.findByUsername(username);
        UserEntity findByUsername = findByUsernameOpt.orElseThrow(() -> new RuntimeException(username + "을 찾을 수 없음"));

        Optional<UserEntity> loginUserOpt = this.lookupService.getCurrentUserEntity();
        if (loginUserOpt.isPresent()) {
            Optional<FollowEntity> existFollow = this.followRepository.findExistFollow(findByUsername.getUid(),
                loginUserOpt.get()
                            .getUid());
            if (existFollow.isPresent()) {
                isFollow = true;
            }
        }

        return ProfileDto.builder()
                         .username(findByUsername.getUsername())
                         .bio(findByUsername.getBio())
                         .image(findByUsername.getImage())
                         .following(isFollow)
                         .build();
    }

    @Transactional
    public ProfileDto followUser(String username) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity()
                                                 .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        UserEntity findByUsername = this.userRepository.findByUsername(username)
                                                       .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 이름입니다."));
        FollowEntity followEntity = FollowEntity.builder()
                                                .followeeEntity(findByUsername)
                                                .followerEntity(loginUser)
                                                .build();
        this.followRepository.save(followEntity);
        return ProfileDto.builder()
                         .username(findByUsername.getUsername())
                         .bio(findByUsername.getBio())
                         .image(findByUsername.getImage())
                         .following(true)
                         .build();
    }

    @Transactional
    public ProfileDto unFollowUser(String username) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity()
                                                 .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        UserEntity findByUsername = this.userRepository.findByUsername(username)
                                                       .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 이름입니다."));
        Optional<FollowEntity> followEntityOpt = this.followRepository.findExistFollow(findByUsername.getUid(),
            loginUser.getUid());
        followEntityOpt.ifPresent(this.followRepository::delete);

        return ProfileDto.builder()
                         .username(findByUsername.getUsername())
                         .bio(findByUsername.getBio())
                         .image(findByUsername.getImage())
                         .following(false)
                         .build();
    }
}
