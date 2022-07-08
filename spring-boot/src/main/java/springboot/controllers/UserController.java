package springboot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.controllers.dto.UserDto;
import springboot.security.JwtCustomToken;
import springboot.services.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    @ResponseBody
    public UserDto login(@RequestBody UserDto.ReqLoginDto reqLoginDto) {
        System.out.println(reqLoginDto);
        try {
            return this.userService.login(reqLoginDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("/users")
    @ResponseBody
    public UserDto register(@RequestBody UserDto.ReqRegisterDto reqRegisterDto) {
        try {
            return this.userService.register(reqRegisterDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/user")
    @ResponseBody
    public UserDto getUserPrivateInfo() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        try {
            System.out.println(authentication);
            return this.userService.getUserPrivateInfo((JwtCustomToken) authentication);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PutMapping("/user")
    @ResponseBody
    public UserDto updateUserInfo(@RequestBody UserDto.ReqUpdateDto reqUpdateDto) {
        return UserDto.builder()
                      .email("inbeom@naver.com")
                      .image(null)
                      .bio("")
                      .token("gasg")
                      .build();
    }

    @GetMapping("/check")
    @ResponseBody
    public UserDto test() {
        return UserDto.builder()
                      .email("inbeom@naver.com")
                      .image(null)
                      .bio("")
                      .token("gasg")
                      .build();
    }
}
