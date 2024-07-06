package real.world.springbootkt.domain.profile

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/profiles/{username}")
    fun profileByUsername(@PathVariable(name = "username") username: String): ProfileResource.Response =
        ProfileResource.Response(profileService.getProfileByUsername(username))

    @PostMapping("/profiles/{username}/follow")
    fun follow(@PathVariable(name = "username") username: String): ProfileResource.Response =
        ProfileResource.Response(profileService.follow(username))

    @DeleteMapping("/profiles/{username}/follow")
    fun unfollow(@PathVariable(name = "username") username: String): ProfileResource.Response =
        ProfileResource.Response(profileService.unfollow(username))

}