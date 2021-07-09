package com.example.demo.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
        verify(playerRepository).save(player);
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
    void shouldDeletePlayer() {
        // when
        when(playerRepository.existsById(1L)).thenReturn(true);
        playerService.deletePlayer(1L);

        // then
        verify(playerRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenPlayerDoesNotExist() {
        // when
        when(playerRepository.existsById(1L)).thenReturn(false);

        // then
        assertThatThrownBy(() -> playerService.deletePlayer(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("player with id " + 1L + " does not exist");

        verify(playerRepository, never()).deleteById(any());

        // when
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        Exception exception = assertThrows(IllegalStateException.class, () ->
                playerService.updatePlayer(1L, "", ""));

        String message = exception.getMessage();
        assertThat(message).isEqualTo("player with id " + 1L + " does not exist");
    }

    @Test
    void shouldUpdatePlayerName() {
        Player playerSpy = Mockito.spy(new Player().setId(1L));

        when(playerRepository.findById(1L)).thenReturn(Optional.ofNullable(playerSpy));

        playerService.updatePlayer(1L, "Bob", "faker@hawkeye.com");

        verify(playerSpy).setName("Bob");
    }

    @Test
    void shouldNotUpdatePlayerNameIfPlayerNameExists() {
        Player playerSpy = Mockito.spy(new Player().setName("Bob"));
        when(playerRepository.findById(1L)).thenReturn(Optional.ofNullable(playerSpy));

        playerService.updatePlayer(1L, "Bob", "");
        verify(playerSpy, never()).setName(any());

        when(playerRepository.findById(1L)).thenReturn(Optional.ofNullable(new Player()));
        when(playerRepository.findPlayerByName("Bob")).thenReturn(Optional.ofNullable(new Player().setName("Bob")));

        Exception exception = assertThrows(IllegalStateException.class, () ->
                playerService.updatePlayer(1L, "Bob", ""));
        String message = exception.getMessage();
        assertThat(message).isEqualTo("name " + "Bob" + " is taken");
    }

    @Test
    void shouldUpdatePlayerEmail() {
        Player playerSpy = Mockito.spy(new Player());

        when(playerRepository.findById(1L)).thenReturn(Optional.ofNullable(playerSpy));

        playerService.updatePlayer(1L, "", "bob@hawkeye.com");

        verify(playerSpy).setEmail("bob@hawkeye.com");
    }

    @Test
    void shouldNotUpdatePlayerEmail() {
        Player playerSpy = Mockito.spy(new Player().setEmail("bob@hawkeye.com"));

        when(playerRepository.findById(1L)).thenReturn(Optional.ofNullable(playerSpy));

        playerService.updatePlayer(1L, "", "bob@hawkeye.com");

        verify(playerSpy, never()).setEmail(any());
    }
}