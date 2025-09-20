package com.example.tabi.quest.actions.hint.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hintId;

    private String hintOne;
    private Integer hintOnePrice;

    private String hintTwo;
    private Integer hintTwoPrice;

    private String hintThree;
    private Integer hintThreePrice;
}
