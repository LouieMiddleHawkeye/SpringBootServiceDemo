package com.example.demo.player;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class PlayerConfig {

    @Bean
    CommandLineRunner commandLineRunner(PlayerRepository repository) {
        return args -> {
            Player roger = new Player()
                    .setName("Roger Federer")
                    .setEmail("roger.federer@hawkeye.com")
                    .setDob(LocalDate.of(1981, Month.AUGUST, 5));

            Player novak = new Player()
                    .setName("Novak Djokovic")
                    .setEmail("novak.djokovic@hawkeye.com")
                    .setDob(LocalDate.of(1987, Month.MAY, 22));

            repository.saveAll(
                    List.of(roger, novak)
            );
        };
    }
}
