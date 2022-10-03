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
        UserEntity targetUser = this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없음"));
        Optional<UserEntity> loginUserOpt = this.lookupService.getCurrentUserEntity();
        boolean isFollowing = loginUserOpt.isPresent() && this.followRepository.findExistFollowByEntity(loginUserOpt.get(), targetUser).isPresent();
        return ProfileDto.builder()
                         .username(targetUser.getUsername())
                         .bio(targetUser.getBio())
                         .image(targetUser.getImage())
                         .following(isFollowing)
                         .build();
    }

    @Transactional
    public ProfileDto followUser(String username) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        UserEntity targetUser = this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("존재하지 않는 유저 이름입니다."));
        FollowEntity followEntity = FollowEntity.builder()
                                                .followeeEntity(targetUser)
                                                .followerEntity(loginUser)
                                                .build();
        this.followRepository.save(followEntity);
        return ProfileDto.builder()
                         .username(targetUser.getUsername())
                         .bio(targetUser.getBio())
                         .image(targetUser.getImage())
                         .following(true)
                         .build();
    }

    @Transactional
    public ProfileDto unFollowUser(String username) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        UserEntity targetUser = this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("존재하지 않는 유저 이름입니다."));
        Optional<FollowEntity> followEntityOpt = this.followRepository.findExistFollow(targetUser.getUid(),
            loginUser.getUid());
        followEntityOpt.ifPresent(this.followRepository::delete);

        return ProfileDto.builder()
                         .username(targetUser.getUsername())
                         .bio(targetUser.getBio())
                         .image(targetUser.getImage())
                         .following(false)
                         .build();
    }
}
