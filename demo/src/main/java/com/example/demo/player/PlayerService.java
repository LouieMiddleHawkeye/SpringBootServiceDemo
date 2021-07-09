package com.example.demo.player;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Tells spring this is a bean and needs to be instantiated
@Service
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public void addNewPlayer(Player player) {
        Optional<Player> playerOptional = playerRepository.findPlayerByName(player.getName());
        if (playerOptional.isPresent()) {
            throw new IllegalStateException("name " + player.getName() + " is taken");
        }
//        log.debug(player.toString());
//        System.out.println(player);
        playerRepository.save(player);
    }

    public void deletePlayer(Long playerId) {
        boolean playerExists = playerRepository.existsById(playerId);
        if (!playerExists) {
            throw new IllegalStateException("player with id " + playerId + " does not exist");
        }
        playerRepository.deleteById(playerId);
    }

    // Says don't need to use any JBQL query. Can use setters to update the entity.
    @Transactional
    public void updatePlayer(Long playerId, String playerName, String playerEmail) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new IllegalStateException(
                "player with id " + playerId + " does not exist"
        ));

        if  (playerName != null && !(playerName.isBlank()) && !Objects.equals(player.getName(), playerName)) {
            Optional<Player> playerOptional = playerRepository.findPlayerByName(playerName);
            if (playerOptional.isPresent()) {
                throw new IllegalStateException("name " + playerOptional.get().getName() + " is taken");
            }
            player.setName(playerName);
        }

        if  (playerEmail != null && !(playerEmail.isBlank()) && !Objects.equals(player.getEmail(), playerEmail)) {
            player.setEmail(playerEmail);
        }
    }
}
