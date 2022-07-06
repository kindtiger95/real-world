package springboot.services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.controllers.dto.UserDto;

@Service
public class UserService {

    public UserDto login(UserDto.ReqLoginDto loginDto) {
    }

    public UserDto register(UserDto.ReqRegisterDto reqRegisterDto) {

    }

    public UserDto getUserPrivateInfo() {
    }

    public UserDto updateUserInfo(@RequestBody UserDto.ReqUpdateDto reqUpdateDto) {
    }
}
