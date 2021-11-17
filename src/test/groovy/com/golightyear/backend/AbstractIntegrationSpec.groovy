package com.golightyear.backend

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.lifecycle.Startables
import spock.lang.Specification

import java.util.stream.Stream

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = Initializer.class)
abstract class AbstractIntegrationSpec extends Specification {
    @Autowired
    Flyway flyway

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static PostgreSQLContainer postgres =
                new PostgreSQLContainer<>("postgres:12.3-alpine")

        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers()

            final def environment = applicationContext.getEnvironment()
            final def testcontainers = new MapPropertySource("testcontainers", createConnectionConfiguration())

            environment.getPropertySources().addFirst(testcontainers)
        }

        private static void startContainers() {
            Startables.deepStart(Stream.of(postgres)).join()
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgres.getJdbcUrl(),
                    "spring.datasource.username", postgres.getUsername(),
                    "spring.datasource.password", postgres.getPassword(),
            )
        }
    }

    def setup() {
        flyway.migrate()
    }

    def cleanup() {
        flyway.clean()
    }
}
