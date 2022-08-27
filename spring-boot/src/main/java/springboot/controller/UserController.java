package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.dto.request.UserReq;
import springboot.domain.dto.response.UserRes;
import springboot.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/login")
    public UserRes login(@RequestBody @Validated UserReq.Login loginDto) {
        return this.userService.loginProcessing(loginDto);
    }

    @PostMapping("/users")
    public UserRes register(@RequestBody @Validated UserReq.Register registerDto) {
        return UserRes.builder()
                      .username("test1")
                      .email("a@a.com")
                      .image("test")
                      .token("test244")
                      .build();
    }

    @GetMapping("/user")
    public UserRes getUserPrivateInfo() {
//        Authentication authentication = SecurityContextHolder.getContext()
//                                                             .getAuthentication();
        return UserRes.builder()
                      .username("test1")
                      .email("a@a.com")
                      .image("test")
                      .token("test244")
                      .build();
    }

    @PutMapping("/user")
    public UserRes updateUserInfo(@RequestBody @Validated UserReq.Update updateDto) {
        return UserRes.builder()
                      .username("test1")
                      .email("a@a.com")
                      .image("test")
                      .token("test244")
                      .build();
    }
}
