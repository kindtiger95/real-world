package springboot.services;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.controllers.dto.UserDto;
import springboot.database.entities.UsersEntity;
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
        UsersEntity usersEntity = this.userRepository.findByEmail(reqLoginDto.getEmail())
                                                    .orElseThrow(() -> new Exception("No exist user."));

        boolean matches = bCryptPasswordEncoder.matches(reqLoginDto.getPassword(), usersEntity.getPassword());
        if (!matches) throw new Exception("Password is not correct.");

        String signedToken = this.jwtUtility.jwtSign(usersEntity.getUid(), usersEntity.getUsername());

        return UserDto.builder()
                      .bio(usersEntity.getBio())
                      .image(usersEntity.getImage())
                      .email(usersEntity.getEmail())
                      .token(signedToken)
                      .username(usersEntity.getUsername())
                      .build();
    }

    public UserDto register(UserDto.ReqRegisterDto reqRegisterDto) throws Exception {
        userRepository.findByEmail(reqRegisterDto.getEmail())
                      .ifPresent(user -> {
                          throw new RuntimeException("error");
                      });

        String encryptedPw = this.bCryptPasswordEncoder.encode(reqRegisterDto.getPassword());
        UsersEntity userEntity = UsersEntity.builder()
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
        UsersEntity usersEntity = this.userRepository.findById(userUid)
                                                   .orElseThrow(() -> new Exception("no exist user."));
        return UserDto.builder()
                      .email(usersEntity.getEmail())
                      .token(credentials)
                      .username(usersEntity.getUsername())
                      .bio(usersEntity.getBio())
                      .image(usersEntity.getImage())
                      .build();
    }
}
