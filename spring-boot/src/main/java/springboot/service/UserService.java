package springboot.service;

import io.jsonwebtoken.Claims;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.common.utility.JwtUtility;
import springboot.domain.dto.request.UserReq.LoginDto;
import springboot.domain.dto.request.UserReq.RegisterDto;
import springboot.domain.dto.request.UserReq.UpdateDto;
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
        return this.getUserResDto(userEntity, token);
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
        return this.getUserResDto(userEntity, token);
    }

    public UserResDto getCurrentUser() {
        UserEntity currentUserEntity = this.getCurrentUserEntity();
        String token = this.jwtUtility.jwtSign(currentUserEntity.getUid(), currentUserEntity.getUsername());
        return getUserResDto(currentUserEntity, token);
    }

    @Transactional
    public UserResDto updateUserInfo(UpdateDto updateDto) {
        UserEntity currentUserEntity = this.getCurrentUserEntity();
        String password = "";
        if (!updateDto.getPassword()
                      .isEmpty()) {
            password = this.bCryptPasswordEncoder.encode(updateDto.getPassword());
        }
        currentUserEntity.changeUserInfo(updateDto.getEmail(), updateDto.getUsername(), password,
            updateDto.getBio(), updateDto.getImage());
        String token = this.jwtUtility.jwtSign(currentUserEntity.getUid(), currentUserEntity.getUsername());
        return this.getUserResDto(currentUserEntity, token);
    }

    private UserEntity getCurrentUserEntity() {
        JwtCustomToken authentication = (JwtCustomToken) SecurityContextHolder.getContext()
                                                                              .getAuthentication();
        Claims claims = authentication.getPrincipal();
        Integer userUid = (Integer) claims.get("userUid");
        String userName = (String) claims.get("userName");

        UserEntity userEntity = this.userRepository.findById(Long.valueOf(userUid))
                                                   .orElseThrow(() -> new RuntimeException("등록되지 않은 유저입니다."));
        if (!userEntity.getUsername()
                       .equals(userName)) {
            throw new RuntimeException("유저 정보가 일치하지 않습니다.");
        }
        return userEntity;
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
