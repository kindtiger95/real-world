package real.world.springbootkt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@ConfigurationPropertiesScan(basePackages = ["real.world.springbootkt"])
class SpringBootKtApplication

fun main(args: Array<String>) {
	runApplication<SpringBootKtApplication>(*args)
}

@RestController
class HealthCheckController {
	@GetMapping("/health-check")
	fun healthCheck() = ResponseEntity<Any>(HttpStatus.OK)
}
