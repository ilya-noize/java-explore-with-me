package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@SpringBootApplication
public class MainServer {
    public static void main(String[] args) {
        SpringApplication.run(MainServer.class, args);
    }
}

@Component
@Profile("test")
@Slf4j
class ApplicationStartupListener {

    @EventListener(ApplicationReadyEvent.class)
    public void printParamsAfterStart(ApplicationReadyEvent event) {
        String dbUrl = event.getApplicationContext().getEnvironment().getProperty("spring.datasource.url");
        String dbUser = event.getApplicationContext().getEnvironment().getProperty("spring.datasource.username");
        String dbPass = event.getApplicationContext().getEnvironment().getProperty("spring.datasource.password");
        String statUrl = event.getApplicationContext().getEnvironment().getProperty("services.stats-service.uri");

        log.warn("URL БД: {}", dbUrl);
        log.warn("Логин: {}", Objects.equals(dbUser, "test") ? dbUser : "user");
        log.warn("Пароль: {}", Objects.equals(dbPass, "test") ? dbPass : "pass");
        log.warn("URL сервиса статистики: {}", statUrl);
    }
}