package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.dto.ProfileDto;
import springboot.service.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profiles/{username}")
    public ProfileDto getUserProfile(@PathVariable String username) {
        return this.profileService.getProfile(username);
    }

    @PostMapping("/profiles/{username}/follow")
    public ProfileDto followUser(@PathVariable String username) {
        return this.profileService.followUser(username);
    }

    @DeleteMapping("/profiles/{username}/follow")
    public ProfileDto unFollowUser(@PathVariable String username) {
        return this.profileService.unFollowUser(username);
    }
}
