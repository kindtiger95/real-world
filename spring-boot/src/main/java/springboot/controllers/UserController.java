package springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.entities.UserDto;

@Controller
public class UserController {
    @PostMapping("/users/login")
    @ResponseBody
    public UserDto signUp()

}
