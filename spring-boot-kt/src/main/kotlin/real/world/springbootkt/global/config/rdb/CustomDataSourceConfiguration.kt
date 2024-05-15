package real.world.springbootkt.global.config.rdb

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
class CustomDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "database.master")
    fun masterHikariConfig(): HikariConfig = HikariConfig()

    @Bean
    @ConfigurationProperties(prefix = "database.slave")
    fun slaveHikariConfig(): HikariConfig = HikariConfig()

    @Bean
    fun masterDataSource(@Qualifier("masterHikariConfig") masterHikariConfig: HikariConfig) =
        HikariDataSource(masterHikariConfig)

    @Bean
    fun slaveDataSource(@Qualifier("slaveHikariConfig") slaveHikariConfig: HikariConfig) =
        HikariDataSource(slaveHikariConfig)
}