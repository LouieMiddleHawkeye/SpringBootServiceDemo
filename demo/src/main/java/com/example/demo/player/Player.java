package com.example.demo.player;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table
@Data
public class Player {

    @Id
    @SequenceGenerator(
            name = "player_sequence",
            sequenceName = "player_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_sequence"
    )
    private Long id;
    private String name;
    private String email;
    //Says right, no need for this to be a column in the database.
    @Transient
    private Integer age;
    private LocalDate dob;

    public Integer getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }
}
