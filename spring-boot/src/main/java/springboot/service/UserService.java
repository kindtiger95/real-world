package springboot.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springboot.common.utility.JwtUtility;
import springboot.domain.dto.UserDto.LoginDto;
import springboot.domain.dto.UserDto.RegisterDto;
import springboot.domain.dto.UserDto.UpdateDto;
import springboot.domain.dto.UserDto.UserResDto;
import springboot.domain.entity.UserEntity;
import springboot.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LookupService lookupService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtility jwtUtility;

    public UserResDto loginProcessing(LoginDto loginDto) {
        UserEntity userEntity = this.userRepository.findByEmail(loginDto.getEmail())
                                                   .orElseThrow(() -> new RuntimeException("유저 없음"));
        String password = userEntity.getPassword();
        boolean matches = this.bCryptPasswordEncoder.matches(loginDto.getPassword(), password);
        if (!matches) {
            throw new RuntimeException("비밀번호 일치하지 않음");
        }

        String token = this.jwtUtility.jwtSign(userEntity.getUid(), userEntity.getUsername());
        return this.getUserResDto(userEntity, token);
    }

    @Transactional
    public UserResDto registerProcessing(RegisterDto registerDto) {
        Optional<UserEntity> userFined = this.userRepository.findByEmail(registerDto.getEmail());
        if (userFined.isPresent()) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        String password = registerDto.getPassword();
        String encodePw = this.bCryptPasswordEncoder.encode(password);
        UserEntity userEntity = UserEntity.builder()
                                          .username(registerDto.getUsername())
                                          .email(registerDto.getEmail())
                                          .password(encodePw)
                                          .build();
        this.userRepository.save(userEntity);
        String token = this.jwtUtility.jwtSign(userEntity.getUid(), userEntity.getUsername());
        return this.getUserResDto(userEntity, token);
    }

    public UserResDto getCurrentUser() {
        UserEntity currentUserEntity = this.lookupService.getCurrentUserEntity()
                                                         .orElseThrow(() -> new RuntimeException("Authentication 없음"));
        String token = this.jwtUtility.jwtSign(currentUserEntity.getUid(), currentUserEntity.getUsername());
        return getUserResDto(currentUserEntity, token);
    }

    @Transactional
    public UserResDto updateUserInfo(UpdateDto updateDto) {
        UserEntity currentUserEntity = this.lookupService.getCurrentUserEntity()
                                                         .orElseThrow(() -> new RuntimeException("Authentication 없음"));
        String password = StringUtils.hasText(updateDto.getPassword()) ? this.bCryptPasswordEncoder.encode(updateDto.getPassword()) : "";
        currentUserEntity.changeUserInfo(updateDto.getEmail(), updateDto.getUsername(), password,
            updateDto.getBio(), updateDto.getImage());
        String token = this.jwtUtility.jwtSign(currentUserEntity.getUid(), currentUserEntity.getUsername());
        return this.getUserResDto(currentUserEntity, token);
    }

    private UserResDto getUserResDto(UserEntity currentUserEntity, String token) {
        return UserResDto.builder()
                         .token(token)
                         .image(currentUserEntity.getImage())
                         .email(currentUserEntity.getEmail())
                         .username(currentUserEntity.getUsername())
                         .bio(currentUserEntity.getBio())
                         .build();
    }
}
