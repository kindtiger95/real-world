package real.world.springbootkt.global.config.rdb

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.OffsetDateTime
import java.util.*

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class AuditOffsetDateTimeConfig {
    @Bean
    fun auditingDateTimeProvider() = DateTimeProvider { Optional.of(OffsetDateTime.now()) }
}