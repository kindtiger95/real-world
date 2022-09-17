package springboot.service;

import io.jsonwebtoken.Claims;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.entity.UserEntity;
import springboot.repository.UserRepository;
import springboot.security.JwtCustomToken;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LookupService {
    private final UserRepository userRepository;

    public Optional<UserEntity> getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        if (!(authentication instanceof JwtCustomToken))
            return Optional.empty();

        JwtCustomToken jwtCustomToken = (JwtCustomToken) authentication;

        Claims claims = jwtCustomToken.getPrincipal();
        Integer userUid = (Integer) claims.get("userUid");
        String userName = (String) claims.get("userName");

        UserEntity userEntity = this.userRepository.findById(Long.valueOf(userUid))
                                                   .orElseThrow(() -> new RuntimeException("등록되지 않은 유저입니다."));
        if (!userEntity.getUsername()
                       .equals(userName)) {
            throw new RuntimeException("유저 정보가 일치하지 않습니다.");
        }
        return Optional.of(userEntity);
    }
}
