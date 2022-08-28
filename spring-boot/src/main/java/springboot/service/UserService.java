package springboot.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.common.utility.JwtUtility;
import springboot.domain.dto.request.UserReq.LoginDto;
import springboot.domain.dto.request.UserReq.RegisterDto;
import springboot.domain.dto.response.UserResDto;
import springboot.domain.entity.UserEntity;
import springboot.repository.UserRepository;
import springboot.security.JwtCustomToken;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
        return UserResDto.builder()
                         .token(token)
                         .image(userEntity.getImage())
                         .email(userEntity.getEmail())
                         .username(userEntity.getUsername())
                         .bio(userEntity.getBio())
                         .build();
    }

    @Transactional
    public UserResDto registerProcessing(RegisterDto registerDto) {
        Optional<UserEntity> userFinded = this.userRepository.findByEmail(registerDto.getEmail());
        if (userFinded.isPresent()) {
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
        return UserResDto.builder()
                         .token(token)
                         .image(userEntity.getImage())
                         .email(userEntity.getEmail())
                         .username(userEntity.getUsername())
                         .bio(userEntity.getBio())
                         .build();
    }

    public UserResDto getCurrentUser() {
        JwtCustomToken authentication = (JwtCustomToken) SecurityContextHolder.getContext()
                                                             .getAuthentication();

    }
}
