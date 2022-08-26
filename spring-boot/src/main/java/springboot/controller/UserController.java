package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {


/*    @PostMapping("/users/login")
    public UserRequest login(@RequestBody ReqLogin reqLogin) {
        System.out.println(reqLogin);
        try {
            return this.userService.login(reqLogin);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("/users")
    public UserRequest register(@RequestBody ReqRegister reqRegister) {
        try {
            return this.userService.register(reqRegister);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/user")
    public UserRequest getUserPrivateInfo() {
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
    public UserRequest updateUserInfo(@RequestBody ReqUpdate reqUpdate) {
        return UserRequest.builder()
                          .email("inbeom@naver.com")
                          .image(null)
                          .bio("")
                          .token("gasg")
                          .build();
    }

    @GetMapping("/check")
    public String test() {
        return "Check";
    }*/
}
