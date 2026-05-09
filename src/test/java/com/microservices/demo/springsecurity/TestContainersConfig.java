package com.microservices.demo.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class TestContainersConfig {
    @Bean
    public GenericContainer<?> keycloakContainer() {
        return new GenericContainer<>("quay.io/keycloak/keycloak:24.0.0")
                .withExposedPorts(8080)
                .withEnv("KEYCLOAK_ADMIN", "admin")
                .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
                .withCommand("start-dev")
                .waitingFor(Wait.forHttp("/").forStatusCode(200));
    }

    @Bean
    public DynamicPropertyRegistrar keycloakProperties(GenericContainer<?> keycloak) {
        return (registry) -> {
            // Get the dynamic host and port
            String host = keycloak.getHost();
            Integer port = keycloak.getMappedPort(8080);
            String issuerUri = String.format("http://%s:%d/realms/master", host, port);

            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> issuerUri);
            registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri", () -> issuerUri);
        };
    }
}