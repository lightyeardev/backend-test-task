package com.golightyear.backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.jdbc.JdbcTestUtils
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.lifecycle.Startables
import spock.lang.Specification

import java.util.stream.Stream

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = Initializer.class)
abstract class AbstractIntegrationSpec extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def cleanup() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "account", "transaction", "balance")
    }

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
}
