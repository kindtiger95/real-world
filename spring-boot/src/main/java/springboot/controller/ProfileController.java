package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.dto.response.ProfileResDto;
import springboot.service.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profiles/{username}")
    public ProfileResDto getUserProfile(@PathVariable String username) {
        return this.profileService.getProfile(username);
    }

    @PostMapping("/profiles/{username}/follow")
    public ProfileResDto followUser(@PathVariable String username) {

    }
/*
    @DeleteMapping("/profiles/{username}/follow")
    public ProfileResDto unFollowUser(@PathVariable String username) {

    }
     */
}
