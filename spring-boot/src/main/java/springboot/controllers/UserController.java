package springboot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springboot.controllers.dto.UserDto;
import springboot.services.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    @ResponseBody
    public UserDto login(@RequestBody UserDto.ReqLoginDto reqLoginDto) {
        return this.userService.login(reqLoginDto);
    }

    @PostMapping("/users")
    @ResponseBody
    public UserDto register(@RequestBody UserDto.ReqRegisterDto reqRegisterDto) {
        return UserDto.builder().email("inbeom@naver.com").image(null).bio("").token("gasg")
                .build();
    }

    @GetMapping("/user")
    @ResponseBody
    public UserDto getUserPrivateInfo() {
        return UserDto.builder().email("inbeom@naver.com").image(null).bio("").token("gasg")
                .build();
    }

    @PutMapping("/user")
    @ResponseBody
    public UserDto updateUserInfo(@RequestBody UserDto.ReqUpdateDto reqUpdateDto) {
        return UserDto.builder().email("inbeom@naver.com").image(null).bio("").token("gasg")
                .build();
    }
}
