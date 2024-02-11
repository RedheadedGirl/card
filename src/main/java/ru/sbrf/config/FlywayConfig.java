package ru.sbrf.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(name = "cardFlyway", initMethod = "migrate")
    public Flyway cardFlyway(DataSource dataSource) {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .locations("classpath:migrations")
                .dataSource(dataSource)
                .load();
    }
}
