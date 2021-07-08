package com.example.demo.player;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// This does the commented out code.
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    // As we know playerRepository works, we declare it as a Mock.
    @Mock
    private PlayerRepository playerRepository;
//    private AutoCloseable autoCloseable;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        // Initialises the playerRepository mock (if there was more would initialise them all).
//        autoCloseable = MockitoAnnotations.openMocks(this);
        playerService = new PlayerService(playerRepository);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    /*

    Because we know the repository class is tested we can just mock it.
    That means we can do this - makes the service test class much faster, as no h2 database has to be made.

     */
    @Test
    void shouldGetPlayers() {
        // when
        playerService.getPlayers();

        // We just need to verify that findAll is called, as we know playerRepository is tested, as is the API method.
        // then
        verify(playerRepository).findAll();
    }

    @Test
    void shouldAddNewPlayer() {
        // given
        String name = "Faker";
        Player player = new Player()
                .setName(name)
                .setEmail("faker@hawkeye.com")
                .setDob(LocalDate.of(1842, Month.FEBRUARY, 28));

        // when
        playerService.addNewPlayer(player);

        // then
        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);

        /*
         This is saying we want to capture the student that was passed
         through when "save" was called by the repository
        */
        verify(playerRepository).save(playerArgumentCaptor.capture());

        // This is the player that was passed through in addNewPlayer
        Player capturedPlayer = playerArgumentCaptor.getValue();

        // We want to check if they are the same
        assertThat(capturedPlayer).isEqualTo(player);
    }

    @Test
    void shouldThrowExceptionWhenNameIsTaken() {
        // given
        String name = "Faker";
        Player player = new Player()
                .setName(name)
                .setEmail("faker@hawkeye.com")
                .setDob(LocalDate.of(1842, Month.FEBRUARY, 28));

        // Given findPlayerByName is called it will return the player just created so that the assert below works
        given(playerRepository.findPlayerByName(anyString())).willReturn(Optional.ofNullable(player));

        // when
        // then
        assertThatThrownBy(() -> playerService.addNewPlayer(player))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("name " + "Faker" + " is taken");

        // Verifies that save was never called
        verify(playerRepository, never()).save(any());
    }

    @Test
    @Disabled
    void deletePlayer() {
    }

    @Test
    @Disabled
    void updatePlayer() {
    }
}