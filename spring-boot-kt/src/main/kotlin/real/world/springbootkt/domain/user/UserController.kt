package real.world.springbootkt.domain.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController(private val userService: UserService) {
    @PostMapping("/users")
    fun register(@RequestBody request: UserResources.Register.Request): UserResources.User {
        return this.userService.register(request)
    }

    @PostMapping("/users/login")
    fun login(@RequestBody request: UserResources.Login.Request): UserResources.User {
        return this.userService.login(request)
    }

    @GetMapping("/user")
    fun me(): UserResources.User {
        return this.userService.me()
    }

    @PutMapping("/user")
    fun update(@RequestBody request: UserResources.Update.Request): UserResources.User {
        return this.userService.update(request)
    }
}