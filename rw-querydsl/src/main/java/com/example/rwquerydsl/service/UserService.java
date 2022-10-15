package com.example.rwquerydsl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.example.rwquerydsl.common.utility.JwtUtility;
import com.example.rwquerydsl.domain.dto.UserDto.LoginDto;
import com.example.rwquerydsl.domain.dto.UserDto.RegisterDto;
import com.example.rwquerydsl.domain.dto.UserDto.UpdateDto;
import com.example.rwquerydsl.domain.dto.UserDto.UserResDto;
import com.example.rwquerydsl.domain.entity.UserEntity;
import com.example.rwquerydsl.repository.jpa.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LookupService lookupService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtility jwtUtility;

    public UserResDto loginProcessing(LoginDto loginDto) {
        UserEntity userEntity = this.userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new RuntimeException("유저 없음"));
        String password = userEntity.getPassword();
        if (!this.bCryptPasswordEncoder.matches(loginDto.getPassword(), password)) {
            throw new RuntimeException("비밀번호 일치하지 않음");
        }

        String token = this.jwtUtility.jwtSign(userEntity.getUid(), userEntity.getUsername());
        return this.getUserResDto(userEntity, token);
    }

    @Transactional
    public UserResDto registerProcessing(RegisterDto registerDto) {
        if (this.userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
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
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("Authentication 없음"));
        String token = this.jwtUtility.jwtSign(loginUser.getUid(), loginUser.getUsername());
        return getUserResDto(loginUser, token);
    }

    @Transactional
    public UserResDto updateUserInfo(UpdateDto updateDto) {
        UserEntity loginUser = this.lookupService.getCurrentUserEntity().orElseThrow(() -> new RuntimeException("Authentication 없음"));
        String password = StringUtils.hasText(updateDto.getPassword()) ? this.bCryptPasswordEncoder.encode(updateDto.getPassword()) : "";
        loginUser.changeUserInfo(updateDto.getEmail(), updateDto.getUsername(), password, updateDto.getBio(), updateDto.getImage());
        String token = this.jwtUtility.jwtSign(loginUser.getUid(), loginUser.getUsername());
        return this.getUserResDto(loginUser, token);
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
