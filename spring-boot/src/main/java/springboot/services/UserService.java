package springboot.services;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.controllers.dto.UserDto;
import springboot.database.entities.UserEntity;
import springboot.database.repositories.UserRepository;
import springboot.security.JwtCustomToken;
import springboot.security.JwtUtility;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;

    public UserDto login(UserDto.ReqLoginDto reqLoginDto) throws Exception {
        UserEntity userEntity = this.userRepository.findByEmail(reqLoginDto.getEmail())
                                                   .orElseThrow(() -> new Exception("No exist user."));

        boolean matches = bCryptPasswordEncoder.matches(reqLoginDto.getPassword(), userEntity.getPassword());
        if (!matches) throw new Exception("Password is not correct.");

        String signedToken = this.jwtUtility.jwtSign(userEntity.getUid(), userEntity.getUsername());

        return UserDto.builder()
                      .bio(userEntity.getBio())
                      .image(userEntity.getImage())
                      .email(userEntity.getEmail())
                      .token(signedToken)
                      .username(userEntity.getUsername())
                      .build();
    }

    public UserDto register(UserDto.ReqRegisterDto reqRegisterDto) throws Exception {
        userRepository.findByEmail(reqRegisterDto.getEmail())
                      .ifPresent(user -> {
                          throw new RuntimeException("error");
                      });

        String encryptedPw = this.bCryptPasswordEncoder.encode(reqRegisterDto.getPassword());
        UserEntity userEntity = UserEntity.builder()
                                          .email(reqRegisterDto.getEmail())
                                          .password(encryptedPw)
                                          .username(reqRegisterDto.getUsername())
                                          .build();
        this.userRepository.save(userEntity);
        userEntity = this.userRepository.findByEmail(reqRegisterDto.getEmail())
                                        .orElseThrow(() -> new Exception("No exist user."));

        String signedToken = this.jwtUtility.jwtSign(userEntity.getUid(), userEntity.getUsername());
        return UserDto.builder()
                      .email(userEntity.getEmail())
                      .token(signedToken)
                      .username(userEntity.getEmail())
                      .bio(userEntity.getBio())
                      .image(userEntity.getImage())
                      .build();
    }

    public UserDto getUserPrivateInfo(JwtCustomToken jwtCustomToken) throws Exception {
        Claims principal = (Claims) jwtCustomToken.getPrincipal();
        String credentials = jwtCustomToken.getCredentials();
        Long userUid = this.jwtUtility.getUserUid(principal);
        UserEntity userEntity = this.userRepository.findById(userUid)
                                                   .orElseThrow(() -> new Exception("no exist user."));
        return UserDto.builder()
                      .email(userEntity.getEmail())
                      .token(credentials)
                      .username(userEntity.getUsername())
                      .bio(userEntity.getBio())
                      .image(userEntity.getImage())
                      .build();
    }
}
