package real.world.springbootkt.domain.user

import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService) {
    @PostMapping("/users/register")
    fun register(@RequestBody request: UserResources.Register.Request): UserResources.User {
        return this.userService.register(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: UserResources.Login.Request): UserResources.User {
        return this.userService.login(request)
    }

//    @GetMapping("/me")
//    fun me(): UserResources.User {
//        return this.userService.me()
//    }

    @PutMapping
    fun update(@RequestBody request: UserResources.Update.Request): UserResources.User {
        return this.userService.update(request)
    }
}