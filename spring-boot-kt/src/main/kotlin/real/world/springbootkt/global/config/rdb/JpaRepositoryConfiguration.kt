package real.world.springbootkt.global.config.rdb

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class JpaRepositoryConfiguration @Autowired constructor(
    @Qualifier("masterDataSource") val masterDataSource: DataSource,
    @Qualifier("slaveDataSource") val slaveDataSource: DataSource,
    val jpaProperties: JpaProperties
) {

    @Bean
    @Primary
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        return EntityManagerFactoryBuilder(createJpaVendorAdapter(), jpaProperties.properties, null)
            .dataSource(masterDataSource)
            .properties(jpaProperties.properties)
            .build().apply { this.setPackagesToScan("real.world.springbootkt") }
    }

    @Bean
    fun slaveEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        return EntityManagerFactoryBuilder(createJpaVendorAdapter(), jpaProperties.properties, null)
            .dataSource(slaveDataSource)
            .properties(jpaProperties.properties)
            .build().apply { this.setPackagesToScan("real.world.springbootkt") }
    }

    @Bean
    @Primary
    fun transactionManager(
        @Qualifier("entityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    @Bean
    fun slaveTransactionManager(
        @Qualifier("slaveEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    private fun createJpaVendorAdapter(): JpaVendorAdapter {
        val adapter = HibernateJpaVendorAdapter()
        adapter.setShowSql(jpaProperties.isShowSql)
        adapter.setDatabase(jpaProperties.database)
        adapter.setDatabasePlatform(jpaProperties.databasePlatform)
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl)
        return adapter
    }
}
