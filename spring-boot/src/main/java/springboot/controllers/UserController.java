package springboot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.entities.UserDto;
import springboot.services.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    @ResponseBody
    public UserDto login(@RequestBody UserDto.Login loginDto) {
        return this.userService.login(loginDto);
    }
}
