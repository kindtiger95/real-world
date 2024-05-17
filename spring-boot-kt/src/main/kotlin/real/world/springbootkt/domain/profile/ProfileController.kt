package real.world.springbootkt.domain.profile

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/profiles/{username}")
    fun profileByUsername(@PathVariable(name = "username") username: String): ProfileResource.Response =
        profileService.getProfileByUsername(username)

    @PostMapping("/profiles/{username}/follow")
    fun follow(@PathVariable(name = "username") username: String): ProfileResource.Response =
        profileService.follow(username)

    @DeleteMapping("/profiles/{username}/follow")
    fun unfollow(@PathVariable(name = "username") username: String): ProfileResource.Response =
        profileService.unfollow(username)

}