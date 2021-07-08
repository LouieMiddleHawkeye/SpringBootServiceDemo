package com.example.demo.player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// some wiring voodoo to wire the repository and spins up a h2 database.
@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    // Delete all entries after each test
    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
    }

    /*

    These 2 methods in reality no need to test as using API built in methods.

     */

    @Test
    void shouldCheckIfPlayerExistsByName() {
        // given
        Player player = new Player()
                .setName("Faker")
                .setEmail("faker@hawkeye.com")
                .setDob(LocalDate.of(1842, Month.FEBRUARY, 28));
        playerRepository.save(player);

        // when
        Boolean exists = playerRepository.existsPlayerByName("Faker");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckIfPlayerDoesNotExistByEmail() {
        // given
        String email = "faker@hawkeye.com";

        // when
        Boolean exists = playerRepository.existsPlayerByEmail("faker@hawkeye.com");

        // then
        assertThat(exists).isFalse();
    }

    /*

    This one you should test though.

     */

    @Test
    void shouldFindPlayerByName() {
        // given
        String name = "Faker";
        Player player = new Player()
                .setName(name)
                .setEmail("faker@hawkeye.com")
                .setDob(LocalDate.of(1842, Month.FEBRUARY, 28));
        playerRepository.save(player);

        // when
        Optional<Player> player1 = playerRepository.findPlayerByName(name);

        // then
        assertThat(player1).isPresent();
    }

    @Test
    void shouldNotFindPlayerByName() {
        // given
        String name = "Faker";

        // when
        Optional<Player> player1 = playerRepository.findPlayerByName(name);

        // then
        assertThat(player1).isNotPresent();
    }
}
