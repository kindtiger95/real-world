package springboot.services;

import org.springframework.stereotype.Service;
import springboot.entities.UserDto;

@Service
public class UserService {

    public UserDto login(UserDto.Login loginDto) {
        System.out.println(loginDto);
        return UserDto.builder().email("inbeom@naver.com").image(null).bio("").token("gasg")
            .build();
    }
}
