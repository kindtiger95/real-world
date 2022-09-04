package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.dto.UserDto.LoginDto;
import springboot.domain.dto.UserDto.RegisterDto;
import springboot.domain.dto.UserDto.UpdateDto;
import springboot.domain.dto.UserDto.UserResDto;
import springboot.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/login")
    public UserResDto login(@RequestBody @Validated LoginDto loginDto) {
        return this.userService.loginProcessing(loginDto);
    }

    @PostMapping("/users")
    public UserResDto register(@RequestBody @Validated RegisterDto registerDto) {
        return this.userService.registerProcessing(registerDto);
    }

    @GetMapping("/user")
    public UserResDto getUserPrivateInfo() {
        return this.userService.getCurrentUser();
    }

    @PutMapping("/user")
    public UserResDto updateUserInfo(@RequestBody @Validated UpdateDto updateDto) {
        return this.userService.updateUserInfo(updateDto);
    }
}
