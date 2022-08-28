package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.dto.request.UserReq.LoginDto;
import springboot.domain.dto.request.UserReq.RegisterDto;
import springboot.domain.dto.request.UserReq.UpdateDto;
import springboot.domain.dto.response.UserResDto;
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
//        Authentication authentication = SecurityContextHolder.getContext()
//                                                             .getAuthentication();
        return UserResDto.builder()
                         .username("test1")
                         .email("a@a.com")
                         .image("test")
                         .token("test244")
                         .build();
    }

    @PutMapping("/user")
    public UserResDto updateUserInfo(@RequestBody @Validated UpdateDto updateDto) {
        return UserResDto.builder()
                         .username("test1")
                         .email("a@a.com")
                         .image("test")
                         .token("test244")
                         .build();
    }
}
