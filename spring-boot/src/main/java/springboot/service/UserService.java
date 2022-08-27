package springboot.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.common.utility.JwtUtility;
import springboot.domain.dto.request.UserReq;
import springboot.domain.dto.response.UserRes;
import springboot.domain.entity.UserEntity;
import springboot.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtility jwtUtility;

    public UserRes loginProcessing(UserReq.Login loginDto) {
        UserEntity userEntity = this.userRepository.findByEmail(loginDto.getEmail())
                                                   .orElseThrow(() -> new RuntimeException("유저 없음"));
        String password = userEntity.getPassword();
        boolean matches = this.bCryptPasswordEncoder.matches(loginDto.getPassword(), password);
        if (!matches) {
            throw new RuntimeException("비밀번호 일치하지 않음");
        }

        String token = this.jwtUtility.jwtSign(userEntity.getUid(), userEntity.getUsername());
        return UserRes.builder()
                      .token(token)
                      .image(userEntity.getImage())
                      .email(userEntity.getEmail())
                      .username(userEntity.getUsername())
                      .bio(userEntity.getBio())
                      .build();
    }
}
