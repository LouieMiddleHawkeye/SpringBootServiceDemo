package com.example.demo.player;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//@ConfigurationProperties(prefix="spring.datasource")
public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Equivalent to SELECT * FROM player WHERE name = ?
    // Player here is the player class, not the database as this is JBQL
    // Possible because player is an entity
    /*
    "Entities in JPA are nothing but POJOs representing data that can be persisted to the database.
    An entity represents a table stored in a database. Every instance of an entity represents a row in the table.
     */
    @Query("SELECT p FROM Player p WHERE p.name = ?1")
    Optional<Player> findPlayerByName(String name);

    Boolean existsPlayerByName(String name);

    Boolean existsPlayerByEmail(String email);
}
